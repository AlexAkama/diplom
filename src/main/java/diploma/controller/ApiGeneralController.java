package diploma.controller;

import diploma.main.Connection;
import diploma.model.GlobalSetting;
import diploma.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static diploma.model.GlobalSettingsValue.YES;

@RestController
public class ApiGeneralController {

    private static final String baseCondition = "is_active=1 AND moderation_status='ACCEPTED' AND time < NOW()";
    private static final double MIN_WEIGHT = 0.25;

    @Value("${persona.title}")
    private String title;
    @Value("${persona.subtitle}")
    private String subtitle;
    @Value("${persona.phone}")
    private String phone;
    @Value("${persona.email}")
    private String email;
    @Value("${persona.copyright}")
    private String copyright;
    @Value("${persona.copyrightFrom}")
    private String copyrightFrom;

    public ApiGeneralController() {
    }

    @GetMapping("/api/init")
    public ResponseEntity<Map<String,String>> getPersonalInformation() {
        HashMap<String, String> personalInformation = new HashMap<>();
        personalInformation.put("title", title);
        personalInformation.put("subtitle", subtitle);
        personalInformation.put("phone", phone);
        personalInformation.put("email", email);
        personalInformation.put("copyright", copyright);
        personalInformation.put("copyrightFrom", copyrightFrom);
        return new ResponseEntity<>(personalInformation, HttpStatus.OK);
    }

    @GetMapping("/api/settings")
    public ResponseEntity<Map<String, Boolean>> getSettings() {
        List<GlobalSetting> globalSettingList;
        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            String hql = "FROM " + GlobalSetting.class.getSimpleName();
            globalSettingList = session.createQuery(hql).getResultList();

            transaction.commit();
        }

        Map<String, Boolean> settingsMap = new HashMap<>();
        for (GlobalSetting globalSetting : globalSettingList) {
            settingsMap.put(
                    globalSetting.getCode(),
                    globalSetting.getValue() == YES
            );
        }

        return new ResponseEntity<>(settingsMap, HttpStatus.OK);
    }

    @GetMapping("/api/tag")
    public ResponseEntity<TagListDto> getTags(@RequestParam(value = "query", required = false) String query) {

        List<TagDto> tags = new ArrayList<>();
        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            String sql = "select t.name, count(*) from tag2post tp"
                    + " inner join tags t on tp.tag_id = t.id"
                    + " where"
                    + " post_id in"
                    + " (select p.id from posts p where " + baseCondition + ")"
                    + " group by tp.tag_id";
            List<Object[]> rows = session.createSQLQuery(sql).getResultList();

            double weight;
            double maxWeight = 0;
            Map<String, Double> tagsMap = new HashMap<>();
            for (Object[] row : rows) {
                BigInteger bigInteger = (BigInteger) row[1];
                weight = bigInteger.intValue();
                if (weight > maxWeight) {
                    maxWeight = weight;
                }
                tagsMap.put((String) row[0], weight);
            }
            for (String key : tagsMap.keySet()) {
                weight = Math.max(tagsMap.get(key) / maxWeight, MIN_WEIGHT);
                tags.add(new TagDto(key, weight));
            }

            transaction.commit();
        }

        return new ResponseEntity<>(new TagListDto(tags), HttpStatus.OK);

    }

    @GetMapping("/api/statistics/all")
    public ResponseEntity<Map<String, Long>> getAllStatistics() {
        Map<String, Long> statistics = new HashMap<>();
        String hql;
        boolean global ;

        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            hql = "from GlobalSetting where code='STATISTICS_IS_PUBLIC'";
            GlobalSetting setting = (GlobalSetting) session.createQuery(hql).uniqueResult();
            global = setting.getValue() == YES;

            transaction.commit();
        }

        User user = new User();
        user.setModerator(true);

        if (user.isModerator() && global) {

            long postCount;
            long likesCount;
            long dislikesCount;
            long viewsCount;
            long firstPublication;

            try (Session session = Connection.getSession()) {
                Transaction transaction = session.beginTransaction();

                hql = "select count(*), sum(viewCount), min(time) from Post";
                Object[] row = (Object[]) session.createQuery(hql).uniqueResult();
                postCount = (long) row[0];
                viewsCount = (long) row[1];
                firstPublication = ((Timestamp) row[2]).getTime()/1000;

                hql = "select count(*) from PostVote group by value order by value";
                List<Long> rows = session.createQuery(hql).getResultList();
                dislikesCount = rows.get(0);
                likesCount = rows.get(1);

                transaction.commit();
            }

            statistics.put("postsCount", postCount);
            statistics.put("likesCount", likesCount);
            statistics.put("dislikesCount", dislikesCount);
            statistics.put("viewsCount", viewsCount);
            statistics.put("firstPublication", firstPublication);

        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }


    private class TagDto {
        private String name;
        private double weight;

        public TagDto(String name, double weight) {
            this.name = name;
            this.weight = weight;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }
    }

    private class TagListDto {
        List<TagDto> tags;

        public TagListDto(List<TagDto> tags) {
            this.tags = tags;
        }

        public List<TagDto> getTags() {
            return tags;
        }

        public void setTags(List<TagDto> tags) {
            this.tags = tags;
        }
    }
}

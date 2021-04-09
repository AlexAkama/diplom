package project.controller;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.config.Connection;
import project.dto.TagDto;
import project.dto.TagListDto;
import project.dto.global.*;
import project.service.GlobalService;

import java.math.BigInteger;
import java.util.*;


@RestController
public class GlobalController {

    private static final String baseCondition = "is_active=1 AND moderation_status='ACCEPTED' AND time < NOW()";
    private static final double MIN_WEIGHT = 0.25;

    private final GlobalService globalService;

    public GlobalController(GlobalService globalService) {
        this.globalService = globalService;
    }

    @GetMapping("/api/init")
    public ResponseEntity<PersonalInfoDto> getPersonalInformation() {
        return globalService.getPersonalInfo();
    }

    @GetMapping("/api/settings")
    public ResponseEntity<GlobalSettingsDto> getSettings() {
        return globalService.getGlobalSettings();
    }

    @GetMapping("/api/tag")
    public ResponseEntity<TagListDto> getTags(@RequestParam(value = "query", required = false) String query) {

        List<TagDto> tags = new ArrayList<>();
        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            //FIXME SQL запрос
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
                weight = (double) (int) (weight * 100) / 100;
                tags.add(new TagDto(key, weight));
            }

            transaction.commit();
        }

        return new ResponseEntity<>(new TagListDto(tags), HttpStatus.OK);

    }

    @GetMapping("/api/calendar")
    public ResponseEntity<CalendarDto> getCalendar(
            @RequestParam("year") int year
    ) {
        String yearsHql = "select date_format(p.time, '%Y') as year from Post p group by year order by year desc";
        String postsHql = "select date_format(p.time, '%Y-%m-%d') as date, count(*) as count from Post p"
                + " where year(p.time) = " + year
                + " and " + baseCondition
                + " group by date"
                + " order by date desc";

        List<String> years;
        List<Object[]> postsInDayList;
        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            years = session.createQuery(yearsHql).getResultList();
            postsInDayList = session.createQuery(postsHql).getResultList();

            transaction.commit();
        }

        Map<String, Long> postsInDay = new HashMap<>();
        for (Object[] row : postsInDayList) {
            postsInDay.put((String) row[0], (Long) row[1]);
        }

        return new ResponseEntity<>(
                new CalendarDto(years, postsInDay),
                HttpStatus.OK);
    }

}

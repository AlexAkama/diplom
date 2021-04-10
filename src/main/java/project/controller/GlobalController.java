package project.controller;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.config.Connection;
import project.dto.global.*;
import project.repository.TagToPostRepository;
import project.service.GlobalService;

import java.math.BigInteger;
import java.util.*;


@RestController
public class GlobalController {

    private static final String baseCondition = "is_active=1 AND moderation_status='ACCEPTED' AND time < NOW()";
    private static final double MIN_WEIGHT = 0.25;

    private final GlobalService globalService;
    private final TagToPostRepository tagToPostRepository;

    public GlobalController(GlobalService globalService,
                            TagToPostRepository tagToPostRepository) {
        this.globalService = globalService;
        this.tagToPostRepository = tagToPostRepository;
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

        List<TagDto> tagListWithWeight = new ArrayList<>();
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
                tagListWithWeight.add(new TagDto(key, weight));
            }

            transaction.commit();
        }

//        List<TagCounter> list = tagToPostRepository.getTagCounterList();
//        list.forEach(el -> System.out.println(el.getName() + el.getCounter()));
//        double maxCounter = list.stream().max(Comparator.comparingLong(TagCounter::getCounter)).get().getCounter();
//        List<TagDto> tagListWithWeight = list.stream()
//                .map(tagCounter -> {
//                    double weight = Math.max(tagCounter.getCounter() / maxCounter, MIN_WEIGHT);
//                    weight = (double) (int) (weight * 100) / 100;
//                    return new TagDto(tagCounter.getName(), weight);
//                })
//                .collect(Collectors.toList());

        return ResponseEntity.ok(new TagListDto(tagListWithWeight));

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

package project.controller;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.config.Connection;
import project.dto.global.*;
import project.dto.post.PostYear;
import project.repository.PostRepository;
import project.service.GlobalService;

import java.util.*;
import java.util.stream.Collectors;


@RestController
public class GlobalController {

    private static final String baseCondition = "is_active=1 AND moderation_status='ACCEPTED' AND time < NOW()";

    private final GlobalService globalService;
    private final PostRepository postRepository;

    public GlobalController(GlobalService globalService, PostRepository postRepository) {
        this.globalService = globalService;
        this.postRepository = postRepository;
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
        return globalService.getTagList();
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

        List<PostYear> list = postRepository.getYearList();
        List<String> yearList = list.stream().map(PostYear::getYear).collect(Collectors.toList());

        return new ResponseEntity<>(
                new CalendarDto(yearList, postsInDay),
                HttpStatus.OK);
    }

}

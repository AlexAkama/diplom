package project.controller;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.config.Connection;
import project.dto.StatDto;
import project.dto.VoteCounterDto;
import project.model.GlobalSetting;
import project.model.User;

import java.util.HashMap;
import java.util.Map;

import static project.model.emun.GlobalSettingsValue.YES;

@RestController
@RequestMapping("/api/statistics/")
public class StatisticController {

    @GetMapping("/all")
    public ResponseEntity<Map<String, Long>> getAllStatistics() {
        Map<String, Long> statistics = new HashMap<>();

        boolean global;
        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            String hql = "from GlobalSetting where code='STATISTICS_IS_PUBLIC'";
            GlobalSetting setting = (GlobalSetting) session.createQuery(hql).uniqueResult();
            global = setting.getValue() == YES;

            transaction.commit();
        }

        //FIXME Тут нужен текущий пользователь
        User user = new User();
        user.setModerator(true);

        if (user.isModerator() || global) {

            StatDto statDto = new StatDto().getBlogResult();
            VoteCounterDto voteCounterDto = voteRepository.getBlogResult();

            statistics.put("postsCount", statDto.getPostsCount());
            statistics.put("likesCount", voteCounterDto.getLikeCounter());
            statistics.put("dislikesCount", voteCounterDto.getDislikeCounter());
            statistics.put("viewsCount", statDto.getViewsCount());
            statistics.put("firstPublication", statDto.getFirstPublication());

        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

}

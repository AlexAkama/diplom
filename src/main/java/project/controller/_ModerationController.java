package project.controller;

import project.config.Connection;
import project.dto.global.GlobalSettingsDto;
import project.dto.LikesDto;
import project.dto.StatDto;
import project.model.GlobalSetting;
import project.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static project.dto.Dto.saveGlobalParameter;
import static project.model.emun.GlobalSettings.*;
import static project.model.emun.GlobalSettingsValue.YES;

@RestController
public class _ModerationController {

    @GetMapping("/api/statistics/all")
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
            LikesDto likesDto = new LikesDto().getBlogResult();

            statistics.put("postsCount", statDto.getPostsCount());
            statistics.put("likesCount", likesDto.getLikeCount());
            statistics.put("dislikesCount", likesDto.getDislikeCount());
            statistics.put("viewsCount", statDto.getViewsCount());
            statistics.put("firstPublication", statDto.getFirstPublication());

        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

    @PostMapping("/api/settings")
    public void setGlobalSettings(@RequestBody GlobalSettingsDto oldSettings) {

        saveGlobalParameter(MULTIUSER_MODE.name(), oldSettings.isMultiUser());
        saveGlobalParameter(POST_PREMODERATION.name(), oldSettings.isPreModeration());
        saveGlobalParameter(STATISTICS_IS_PUBLIC.name(), oldSettings.isPublicStatistic());

    }
}

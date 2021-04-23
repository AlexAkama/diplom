package project.service.impementation;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.dto.statistic.StatisticDto;
import project.exception.UnauthorizedException;
import project.model.GlobalSetting;
import project.model.User;
import project.repository.GlobalSettingRepository;
import project.service.PostService;
import project.service.StatisticService;

import java.util.Optional;

import static project.model.emun.GlobalSettings.STATISTICS_IS_PUBLIC;
import static project.model.emun.GlobalSettingsValue.YES;

@Service
public class StatisticServiceImpl implements StatisticService {

    private final GlobalSettingRepository globalSettingRepository;
    private final PostService postService;

    public StatisticServiceImpl(GlobalSettingRepository globalSettingRepository,
                                PostService postService) {
        this.globalSettingRepository = globalSettingRepository;
        this.postService = postService;
    }

    @Override
    public ResponseEntity<StatisticDto> getAllStatistic() {
        StatisticDto response;
        if (checkUser()) {
            response = postService.getAllStatistic();
        } else throw new UnauthorizedException("Требуется авторизация");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<StatisticDto> getUserStatistic() {
        StatisticDto response;
        if (checkUser()) {
            //FIXME Тут нужен видимо id текущего пользователя
            long userId = 10;
            response = postService.getUserStatistic(userId);
        } else throw new UnauthorizedException("Требуется авторизация");
        return ResponseEntity.ok(response);
    }

    private boolean checkUser() {
        Optional<GlobalSetting> optionalPublicStatistic =
                globalSettingRepository.findByCode(STATISTICS_IS_PUBLIC.name());
        boolean statisticIsPublic =
                optionalPublicStatistic.isPresent() && optionalPublicStatistic.get().getValue() == YES;

        //FIXME Тут нужен текущий пользователь, причем модератор
        User user = new User();
        user.setModerator(true);

        return statisticIsPublic || user.isModerator();
    }

}

package project.service.impementation;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.dto.statistic.StatisticDto;
import project.exception.UnauthorizedException;
import project.model.GlobalSetting;
import project.model.User;
import project.repository.GlobalSettingRepository;
import project.repository._UserRepository;
import project.service.PostService;
import project.service.StatisticService;

import java.util.Optional;

import static project.model.emun.GlobalSettings.STATISTICS_IS_PUBLIC;
import static project.model.emun.GlobalSettingsValue.YES;

@Service
public class StatisticServiceImpl implements StatisticService {

    private final GlobalSettingRepository globalSettingRepository;
    private final PostService postService;
    private final _UserRepository userRepository;

    public StatisticServiceImpl(GlobalSettingRepository globalSettingRepository,
                                PostService postService,
                                _UserRepository userRepository) {
        this.globalSettingRepository = globalSettingRepository;
        this.postService = postService;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<StatisticDto> getAllStatistic() {
        Optional<GlobalSetting> optionalPublicStatistic =
                globalSettingRepository.findByCode(STATISTICS_IS_PUBLIC.name());
        boolean statisticIsPublic =
                optionalPublicStatistic.isPresent() && optionalPublicStatistic.get().getValue() == YES;

        StatisticDto response;
        if (statisticIsPublic || checkModerator()) {
            response = postService.getAllStatistic();
        } else throw new UnauthorizedException("Требуется авторизация");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<StatisticDto> getUserStatistic() {
        StatisticDto response;
        User user = checkUser();
        if (user != null) {
            //FIXME Тут нужен видимо id текущего пользователя
            response = postService.getUserStatistic(user.getId());
        } else throw new UnauthorizedException("Требуется авторизация");
        return ResponseEntity.ok(response);
    }

    private User checkUser() {
        //FIXME Тут нужен текущий пользователь, причем модератор
        return userRepository.findByEmail("vasya@gmail.com").orElseThrow();
    }

    private boolean checkModerator() {
        return checkUser().isModerator();
    }

}

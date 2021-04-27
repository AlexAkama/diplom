package project.service.impementation;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.dto.statistic.StatisticDto;
import project.exception.UnauthorizedException;
import project.exception.UserNotFoundException;
import project.model.GlobalSetting;
import project.model.User;
import project.repository.GlobalSettingRepository;
import project.service.*;

import java.util.Optional;

import static project.model.emun.GlobalSettings.STATISTICS_IS_PUBLIC;
import static project.model.emun.GlobalSettingsValue.YES;

@Service
public class StatisticServiceImpl implements StatisticService {

    private final GlobalSettingRepository globalSettingRepository;
    private final PostService postService;
    private final UserService userService;

    public StatisticServiceImpl(GlobalSettingRepository globalSettingRepository,
                                PostService postService,
                                UserService userService) {
        this.globalSettingRepository = globalSettingRepository;
        this.postService = postService;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<StatisticDto> getAllStatistic() throws UserNotFoundException, UnauthorizedException {
        Optional<GlobalSetting> optionalPublicStatistic =
                globalSettingRepository.findByCode(STATISTICS_IS_PUBLIC.name());
        boolean statisticIsPublic =
                optionalPublicStatistic.isPresent() && optionalPublicStatistic.get().getValue() == YES;

        User user = userService.checkUser();
        StatisticDto response;
        if (statisticIsPublic || user.isModerator()) {
            response = postService.getAllStatistic();
        } else throw new UnauthorizedException("Требуется авторизация");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<StatisticDto> getUserStatistic() throws UserNotFoundException, UnauthorizedException {
        User user = userService.checkUser();
        StatisticDto response = postService.getUserStatistic(user.getId());
        return ResponseEntity.ok(response);
    }

}

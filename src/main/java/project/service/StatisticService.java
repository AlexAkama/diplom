package project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.dto.statistic.StatisticDto;
import project.exception.UnauthorizedException;
import project.model.GlobalSetting;
import project.repository.GlobalSettingRepository;

import java.util.Optional;

import static project.model.enums.GlobalSettings.STATISTICS_IS_PUBLIC;
import static project.model.enums.GlobalSettingsValue.YES;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final GlobalSettingRepository globalSettingRepository;
    private final PostService postService;
    private final UserService userService;

    public ResponseEntity<StatisticDto> getAllStatistic() throws UnauthorizedException {
        Optional<GlobalSetting> optionalPublicStatistic =
                globalSettingRepository.findByCode(STATISTICS_IS_PUBLIC.name());
        boolean statisticIsPublic =
                optionalPublicStatistic.isPresent() && optionalPublicStatistic.get().getValue() == YES;

        var user = userService.checkUser();
        StatisticDto response;
        if (statisticIsPublic || user.isModerator()) {
            response = postService.getAllStatistic();
        } else throw new UnauthorizedException("Требуется авторизация");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<StatisticDto> getUserStatistic() throws UnauthorizedException {
        var user = userService.checkUser();
        StatisticDto response = postService.getUserStatistic(user.getId());
        return ResponseEntity.ok(response);
    }

}

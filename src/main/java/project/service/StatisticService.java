package project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.dto.statistic.StatisticDto;
import project.exception.UnauthorizedException;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final GlobalService globalService;
    private final PostService postService;
    private final UserService userService;

    public ResponseEntity<StatisticDto> getAllStatistic() throws UnauthorizedException {
        StatisticDto response = postService.getAllStatistic();
        if (!globalService.statisticIsPublic()) {
            var user = userService.checkUser();
            if (user.isModerator()) {
                return ResponseEntity.ok(response);
            } else throw new UnauthorizedException("Информация для модератора");
        }
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<StatisticDto> getUserStatistic() throws UnauthorizedException {
        var user = userService.checkUser();
        StatisticDto response = postService.getUserStatistic(user.getId());
        return ResponseEntity.ok(response);
    }

}

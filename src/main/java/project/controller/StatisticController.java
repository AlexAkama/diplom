package project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.statistic.StatisticDto;
import project.service.StatisticService;

@RestController
@RequestMapping("/api/statistics/")
public class StatisticController {

    public final StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/all")
    public ResponseEntity<StatisticDto> getAllStatistics() {
        return statisticService.getAllStatistic();
    }

//    @GetMapping("/my")
//    public ResponseEntity<Map<String, Long>> getMyStatistics() {
//        //Текущий пользователь
//        User user = new User();
//        user.setId(10);
//
//        Map<String, Long> statistics = new HashMap<>();
//        int id = user.getId();
//
//        StatDto statDto = new StatDto().getUserResult(id);
//        VoteCounterView voteCounterDto = voteRepository.getUserResult(id);
//
//        statistics.put("postsCount", statDto.getPostsCount());
//        statistics.put("likesCount", voteCounterDto.getLikeCounter());
//        statistics.put("dislikesCount", voteCounterDto.getDislikeCounter());
//        statistics.put("viewsCount", statDto.getViewsCount());
//        statistics.put("firstPublication", statDto.getFirstPublication());
//
//        return new ResponseEntity<>(statistics, HttpStatus.OK);
//    }

}

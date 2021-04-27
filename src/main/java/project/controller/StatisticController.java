package project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.statistic.StatisticDto;
import project.exception.UnauthorizedException;
import project.exception.UserNotFoundException;
import project.service.StatisticService;

@RestController
@RequestMapping("/api/statistics/")
public class StatisticController {

    public final StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/all")
    public ResponseEntity<StatisticDto> getAllStatistics() throws UserNotFoundException, UnauthorizedException {
        return statisticService.getAllStatistic();
    }

    @GetMapping("/my")
    public ResponseEntity<StatisticDto> getMyStatistics() throws UserNotFoundException, UnauthorizedException {
        return statisticService.getUserStatistic();
    }

}

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

    @GetMapping("/my")
    public ResponseEntity<StatisticDto> getMyStatistics() {
        return statisticService.getUserStatistic();
    }

}

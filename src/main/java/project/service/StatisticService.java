package project.service;

import org.springframework.http.ResponseEntity;
import project.dto.statistic.StatisticDto;

public interface StatisticService {

    ResponseEntity<StatisticDto> getAllStatistic();

    ResponseEntity<StatisticDto> getUserStatistic();

}

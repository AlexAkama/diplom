package project.service;

import org.springframework.http.ResponseEntity;
import project.dto.statistic.StatisticDto;
import project.exception.NotFoundException;
import project.exception.UnauthorizedException;

public interface StatisticService {

    ResponseEntity<StatisticDto> getAllStatistic() throws NotFoundException, UnauthorizedException;

    ResponseEntity<StatisticDto> getUserStatistic() throws NotFoundException, UnauthorizedException;

}

package project.service;

import org.springframework.http.ResponseEntity;
import project.dto.statistic.StatisticDto;
import project.exception.UnauthorizedException;
import project.exception.UserNotFoundException;

public interface StatisticService {

    ResponseEntity<StatisticDto> getAllStatistic() throws UserNotFoundException, UnauthorizedException;

    ResponseEntity<StatisticDto> getUserStatistic() throws UserNotFoundException, UnauthorizedException;

}

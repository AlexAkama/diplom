package project.service;

import org.springframework.http.ResponseEntity;
import project.dto.main.AppResponse;
import project.exception.*;

public interface VoteService {

    ResponseEntity<AppResponse> setLike(long postId) throws NotFoundException, UnauthorizedException;

    ResponseEntity<AppResponse> setDislike(long postId) throws NotFoundException, UnauthorizedException;

}

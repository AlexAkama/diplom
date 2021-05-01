package project.service;

import org.springframework.http.ResponseEntity;
import project.dto.main.AppResponse;
import project.exception.*;

public interface VoteService {

    ResponseEntity<? extends AppResponse> setLike(long postId) throws NotFoundException, UnauthorizedException;

    ResponseEntity<? extends AppResponse> setDislike(long postId) throws NotFoundException, UnauthorizedException;

}

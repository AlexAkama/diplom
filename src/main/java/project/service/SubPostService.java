package project.service;

import org.springframework.http.ResponseEntity;
import project.dto.main.OkResponse;
import project.dto.moderation.ModerationRequest;
import project.exception.*;

public interface SubPostService {

    ResponseEntity<OkResponse> setModerationDecision(ModerationRequest request) throws UserNotFoundException, UnauthorizedException, ObjectNotFoundException;
}

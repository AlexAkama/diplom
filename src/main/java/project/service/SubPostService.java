package project.service;

import org.springframework.http.ResponseEntity;
import project.dto.comment.CommentRequest;
import project.dto.comment.CommentResponse;
import project.dto.main.OkResponse;
import project.dto.moderation.ModerationRequest;
import project.exception.*;

public interface SubPostService {

    ResponseEntity<OkResponse> setModerationDecision(ModerationRequest request)
            throws NotFoundException, UnauthorizedException;

    ResponseEntity<CommentResponse> addComment(CommentRequest request) throws NotFoundException, UnauthorizedException;
}

package project.service;

import org.springframework.http.ResponseEntity;
import project.dto.comment.CommentRequest;
import project.dto.comment.CommentResponse;
import project.dto.main.AppResponse;
import project.dto.moderation.ModerationRequest;
import project.exception.NotFoundException;
import project.exception.UnauthorizedException;

public interface SubPostService {

    ResponseEntity<AppResponse> setModerationDecision(ModerationRequest request)
            throws NotFoundException, UnauthorizedException;

    ResponseEntity<CommentResponse> addComment(CommentRequest request) throws NotFoundException, UnauthorizedException;
}

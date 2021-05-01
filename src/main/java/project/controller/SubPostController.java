package project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.dto.comment.CommentRequest;
import project.dto.comment.CommentResponse;
import project.dto.main.OkResponse;
import project.dto.moderation.ModerationRequest;
import project.exception.*;
import project.service.SubPostService;

@Controller
@RequestMapping("/api")
public class SubPostController {

    private final SubPostService subPostService;

    public SubPostController(SubPostService subPostService) {
        this.subPostService = subPostService;
    }

    @PostMapping("/moderation")
    public ResponseEntity<OkResponse> setModerationDecision(
            @RequestBody ModerationRequest request
    ) throws NotFoundException, UnauthorizedException {
        return subPostService.setModerationDecision(request);
    }

    @PostMapping("/comment")
    public ResponseEntity<CommentResponse> addComment(
            @RequestBody CommentRequest request
            ) throws UnauthorizedException, NotFoundException {
        return subPostService.addComment(request);
    }

}

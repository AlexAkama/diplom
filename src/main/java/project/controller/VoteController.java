package project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.dto.main.AppResponse;
import project.dto.vote.VoteRequest;
import project.exception.NotFoundException;
import project.exception.UnauthorizedException;
import project.service.VoteService;

@Controller
@RequestMapping("/api/post")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("like")
    public ResponseEntity<AppResponse> setLike(
            @RequestBody VoteRequest request
    ) throws NotFoundException, UnauthorizedException {
        return voteService.setLike(request.getPostId());
    }

    @PostMapping("dislike")
    public ResponseEntity<AppResponse> setDislike(
            @RequestBody VoteRequest request
    ) throws NotFoundException, UnauthorizedException {
        return voteService.setDislike(request.getPostId());
    }

}

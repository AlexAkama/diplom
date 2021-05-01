package project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
    ) throws UserNotFoundException, ObjectNotFoundException, UnauthorizedException {
        return subPostService.setModerationDecision(request);
    }

}

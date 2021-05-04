package project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.dto.comment.CommentRequest;
import project.dto.comment.CommentResponse;
import project.dto.image.ImageResponse;
import project.dto.main.AppResponse;
import project.dto.moderation.ModerationRequest;
import project.exception.*;
import project.service.ImageService;
import project.service.SubPostService;

@Controller
@RequestMapping("/api")
public class SubPostController {

    private final SubPostService subPostService;
    private final ImageService imageService;

    public SubPostController(SubPostService subPostService,
                             ImageService imageService) {
        this.subPostService = subPostService;
        this.imageService = imageService;
    }

    @PostMapping("/moderation")
    public ResponseEntity<AppResponse> setModerationDecision(
            @RequestBody ModerationRequest request
    ) throws NotFoundException, UnauthorizedException, ForbiddenException {
        return subPostService.setModerationDecision(request);
    }

    @PostMapping("/comment")
    public ResponseEntity<CommentResponse> addComment(
            @RequestBody CommentRequest request
    ) throws UnauthorizedException, NotFoundException {
        return subPostService.addComment(request);
    }

    @PostMapping("/image")
    public ResponseEntity<ImageResponse> saveImage(
            @RequestParam("image") MultipartFile file
    ) throws BadRequestException, UnauthorizedException, NotFoundException, ImageSuccess, InternalServerException {
        return imageService.save(file);
    }
}

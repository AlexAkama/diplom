package project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.post.*;
import project.exception.*;
import project.service.PostService;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
//    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<PostListDto> getPostList(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("mode") String mode
    ) {
        return postService.getAnnounceList(offset, limit, mode);
    }

    @PostMapping
    public ResponseEntity<PostResponse> addPost(
            @RequestBody PostRequest request
    ) throws NotFoundException, UnauthorizedException, ForbiddenException {
        return postService.addPost(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable long id) throws NotFoundException {
        return postService.getPostForResponse(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> editPost(
            @PathVariable long id,
            @RequestBody PostRequest request
    ) throws ForbiddenException, UnauthorizedException, NotFoundException {
        return postService.updatePost(id, request);
    }

    @GetMapping("/byTag")
    public ResponseEntity<PostListDto> getByTag(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("tag") String tag
    ) throws NotFoundException {
        return postService.getAnnounceListByTag(offset, limit, tag);
    }

    @GetMapping("/byDate")
    public ResponseEntity<PostListDto> getByDate(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("date") String date
    ) {
        return postService.getAnnounceListByDate(offset, limit, date);
    }


    @GetMapping("/search")
    public ResponseEntity<PostListDto> getSearchList(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("query") String search
    ) {
        return postService.getAnnounceListBySearch(offset, limit, search);
    }

    @GetMapping("/moderation")
    public ResponseEntity<PostListDto> getToModerationList(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("status") String status
    ) throws NotFoundException, UnauthorizedException {
        return postService.getAnnounceListToModeration(offset, limit, status);
    }

    @GetMapping("/my")
    public ResponseEntity<PostListDto> getUserPostStatistic(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("status") String status
    ) throws NotFoundException, UnauthorizedException {
        return postService.getAnnounceListByAuthUser(offset, limit, status);
    }

}

package project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.post.PostDto;
import project.dto.post.PostListDto;
import project.service.PostService;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("")
    public ResponseEntity<PostListDto> getPostList(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("mode") String mode
    ) {
        return postService.getAnnounceList(offset, limit, mode);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable int id) {
        return postService.getPost(id);
    }

    @GetMapping("/byTag")
    public ResponseEntity<PostListDto> getByTag(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("tag") String tag
    ) {
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

//    @GetMapping("/my")
//    public ResponseEntity<PostListDto> getMy(
//            @RequestParam("offset") int offset,
//            @RequestParam("limit") int limit,
//            @RequestParam("status") String status
//    ) {
//
//        //FIXME --- ИМИТАЦИЯ АВТОРИЗАЦИИ ---
//        int id = 10;
//        String byId = "p.user.id= " + id + " and ";
//
//        String condition = "";
//        PostState postState = PostState.valueOf(status.toUpperCase());
//        switch (postState) {
//            case PENDING:
//                condition = "p.isActive=1 and p.moderationStatus='NEW'";
//                break;
//            case DECLINED:
//                condition = "p.isActive=1 and p.moderationStatus='DECLINED'";
//                break;
//            case PUBLISHED:
//                condition = "p.isActive=1 and p.moderationStatus='ACCEPTED'";
//                break;
//            case INACTIVE:
//                condition = " p.isActive=0";
//                break;
//        }
//
//        return new ResponseEntity<>(
//                new PostListDto().makeAnnounces(byId + condition, offset, limit),
//                HttpStatus.OK);
//    }

}

package project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

//    @GetMapping("/{id}")
//    public ResponseEntity<PostDto> getPostById(@PathVariable int id) {
//
//        //FIXME переделать HQL
//        String hql = "from Post p"
//                + " where " + baseCondition
//                + " and p.id = " + id;
//
//        Post post;
//        try (Session session = Connection.getSession()) {
//            Transaction transaction = session.beginTransaction();
//
//            post = (Post) session.createQuery(hql).uniqueResult();
//
//            transaction.commit();
//        }
//
//        if (post == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        return new ResponseEntity<>(
//                new PostDto().make(post),
//                HttpStatus.OK);
//    }

//    @GetMapping("/search")
//    public ResponseEntity<PostListDto> getSearchList(
//            @RequestParam("offset") int offset,
//            @RequestParam("limit") int limit,
//            @RequestParam("query") String search
//    ) {
//
//        String selectCondition = "text like '%:text%'"
//                + " OR title like '%:text%'"
//                + " OR id IN (select pc.post.id from PostComment pc where pc.text like '%:text%')"
//                + " OR user_id IN (select u.id from User u where u.name like '%:text%')"
//                + " OR id IN (select ttp.post.id from TagToPost ttp where ttp.tag.id"
//                + " in (select t.id from Tag t where t.name like '%:text%'))";
//        selectCondition = selectCondition.replaceAll(":text", search);
//
//        return new ResponseEntity<>(
//                new PostListDto().makeAnnounces(selectCondition, offset, limit),
//                HttpStatus.OK);
//    }

//    @GetMapping("/byTag")
//    public ResponseEntity<PostListDto> getByTag(
//            @RequestParam("offset") int offset,
//            @RequestParam("limit") int limit,
//            @RequestParam("tag") String tag
//    ) {
//
//        String byTagCondition = "id in (select ttp.post.id from TagToPost ttp where ttp.tag.id = " +
//                "(select t.id from Tag t where t.name = '" + tag + "'))";
//
//        return new ResponseEntity<>(
//                new PostListDto().makeAnnounces(byTagCondition, offset, limit, PostViewMode.RECENT),
//                HttpStatus.OK);
//    }

//    @GetMapping("/byDate")
//    public ResponseEntity<PostListDto> getByDate(
//            @RequestParam("offset") int offset,
//            @RequestParam("limit") int limit,
//            @RequestParam("date") String date
//    ) {
//        String byDateCondition = "date_format(time, '%Y-%m-%d') = '" + date + "'";
//
//        return new ResponseEntity<>(
//                new PostListDto().makeAnnounces(byDateCondition, offset, limit),
//                HttpStatus.OK);
//    }

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

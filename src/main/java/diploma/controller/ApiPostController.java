package diploma.controller;

import diploma.main.Connection;
import diploma.model.Post;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static diploma.main.Dto.*;

@RestController
public class ApiPostController {

    @GetMapping("/api/post")
    public ResponseEntity<PostListDto> getPostList(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("mode") String mode
    ) {
        //ASK возможна ошибка если в mode будет передана фигня
        // http://127.0.0.1:8080/api/post?offset=0&limit=10&mode=recent
        PostViewMode postMode = PostViewMode.valueOf(mode.toUpperCase());

        return new ResponseEntity<>(
                new PostListDto().makeAnnounces(offset, limit, postMode),
                HttpStatus.OK);
    }

    @GetMapping("/api/post/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable int id) {

        //FIXME переделать HQL
        String hql = "from Post p"
                + " where " + baseCondition
                + " and p.id = " + id;

        Post post;
        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            post = (Post) session.createQuery(hql).uniqueResult();

            transaction.commit();
        }

        if (post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                new PostDto().make(post),
                HttpStatus.OK);
    }

    @GetMapping("/api/post/search")
    public ResponseEntity<PostListDto> getSearchList(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("query") String search
    ) {

        String selectCondition = "text like '%:text%'"
                + " OR title like '%:text%'"
                + " OR id IN (select pc.post.id from PostComment pc where pc.text like '%:text%')"
                + " OR user_id IN (select u.id from User u where u.name like '%:text%')"
                + " OR id IN (select ttp.post.id from TagToPost ttp where ttp.tag.id"
                + " in (select t.id from Tag t where t.name like '%:text%'))";
        selectCondition = selectCondition.replaceAll(":text", search);

        return new ResponseEntity<>(
                new PostListDto().makeAnnounces(selectCondition, offset, limit),
                HttpStatus.OK);
    }

    @GetMapping("/api/post/byTag")
    public ResponseEntity<PostListDto> getByTag(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("tag") String tag
    ) {

        String byTagCondition = "id in (select ttp.post.id from TagToPost ttp where ttp.tag.id = " +
                "(select t.id from Tag t where t.name = '" + tag + "'))";

        return new ResponseEntity<>(
                new PostListDto().makeAnnounces(byTagCondition, offset, limit, PostViewMode.RECENT),
                HttpStatus.OK);
    }

    @GetMapping("/api/post/byDate")
    public ResponseEntity<PostListDto> getByDate(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("date") String date
    ) {
        String byDateCondition = "date_format(time, '%Y-%m-%d') = '" + date + "'";

        return new ResponseEntity<>(
                new PostListDto().makeAnnounces(byDateCondition, offset, limit),
                HttpStatus.OK);
    }

}

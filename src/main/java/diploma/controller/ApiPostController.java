package diploma.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import diploma.main.Connection;
import diploma.model.Post;
import diploma.model.PostComment;
import diploma.model.PostVote;
import diploma.model.TagToPost;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
public class ApiPostController {

    private static final String baseCondition = "p.isActive=1 AND p.moderationStatus='ACCEPTED' AND p.time < NOW()";

    @GetMapping("/api/post")
    public ResponseEntity getPostList(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("mode") String mode
    ) {
        return new ResponseEntity(
                new PostListDto("", offset, limit, getSort(mode)),
                HttpStatus.OK);
    }

    @GetMapping("/api/post/{id}")
    public ResponseEntity getPostById(@PathVariable int id) {

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
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(
                new PostDto(post),
                HttpStatus.OK);
    }

    @GetMapping("/api/post/search")
    public ResponseEntity getSearchList(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("query") String searchText
    ) {

        String selectCondition = "text like '%:text%'"
                + " OR title like '%:text%'"
                + " OR id IN (select pc.post.id from PostComment pc where pc.text like '%:text%')"
                + " OR user_id IN (select u.id from User u where u.name like '%:text%')"
                + " OR id IN (select ttp.post.id from TagToPost ttp where ttp.tag.id"
                + " in (select t.id from Tag t where t.name like '%:text%'))";
        selectCondition = selectCondition.replaceAll(":text", searchText);

        return new ResponseEntity(
                new PostListDto(selectCondition, offset, limit),
                HttpStatus.OK);
    }

    @GetMapping("/api/post/byTag")
    public ResponseEntity getByTag(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("tag") String tag
    ) {

        String byTagCondition = "id in (select ttp.post.id from TagToPost ttp where ttp.tag.id = " +
                "(select t.id from Tag t where t.name = '" + tag + "'))";

        return new ResponseEntity(
                new PostListDto(byTagCondition, offset, limit),
                HttpStatus.OK);
    }

    @GetMapping("/api/post/byDate")
    public ResponseEntity getByDate(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("date") String date
    ) {
        String byDateCondition = "date_format(time, '%Y-%m-%d') = '" + date + "'";

        return new ResponseEntity(
                new PostListDto(byDateCondition, offset, limit),
                HttpStatus.OK);
    }

    @GetMapping("/api/calendar")
    public ResponseEntity getCalendar(
            @RequestParam("year") int year
    ) {
        String yearsHql = "select date_format(p.time, '%Y') as year from Post p group by year order by year desc";
        String postsHql = "select date_format(p.time, '%Y-%m-%d') as date, count(*) as count from Post p"
                + " where year(p.time) = " + year
                + " and " + baseCondition
                + " group by date"
                + " order by date desc";

        List<String> years;
        List<Object[]> postsInDayList;
        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            years = session.createQuery(yearsHql).getResultList();
            postsInDayList = session.createQuery(postsHql).getResultList();

            transaction.commit();
        }

        Map<String, Long> postsInDay = new HashMap<>();
        for (Object[] row : postsInDayList) {
            postsInDay.put((String) row[0], (Long) row[1]);
        }

        return new ResponseEntity(
                new CalendarDto(years, postsInDay),
                HttpStatus.OK);
    }

    private static SortDescription getSort(String name) {
        SortDescription sort;
        switch (name) {
            case "recent":
                sort = SortDescription.RECENT;
                break;
            case "early":
                sort = SortDescription.EARLY;
                break;
            case "popular":
                sort = SortDescription.POPULAR;
                break;
            case "best":
                sort = SortDescription.BEST;
                break;
            default:
                sort = SortDescription.DEFAULT;
        }
        return sort;
    }

    private static long dateToLong(Date date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(
                    date.toString()
            ).getTime() / 1000;
        } catch (ParseException e) {
            return -1;
        }
    }


    private static class PostListDto {
        private int count;
        private List<PostDto> posts = new ArrayList<>();

        public PostListDto(String conditions, int offset, int limit) {
            this(conditions, offset, limit, SortDescription.DEFAULT);
        }

        public PostListDto(String conditions, int offset, int limit, SortDescription sort) {

            String baseHql = "FROM Post p WHERE " + baseCondition;

            String orderByTime = "";
            if (sort == SortDescription.EARLY) {
                orderByTime = " ORDER BY p.time";
            } else if (sort == SortDescription.RECENT) {
                orderByTime = " ORDER BY p.time DESC";
            }

            List<Post> postList;
            try (Session session = Connection.getSession()) {
                Transaction transaction = session.beginTransaction();

                if (!conditions.isEmpty()) {
                    conditions = " AND (" + conditions + ")";
                }
                String hql = baseHql + conditions;
                count = (int) session.createQuery(hql).getResultStream().count();

                hql += orderByTime;
                postList = session.createQuery(hql)
                        .setFirstResult(offset)
                        .setMaxResults(limit)
                        .getResultList();

                for (Post post : postList) {
                    posts.add(new PostDto(post));
                }

                transaction.commit();
            }

            if (sort == SortDescription.POPULAR) {
                posts.sort(Comparator.comparing(PostDto::getCommentCount)
                        .reversed());
            }
            if (sort == SortDescription.BEST) {
                posts.sort(Comparator.comparing(PostDto::getLikeCount)
                        .thenComparing((o1, o2) -> {
                            if (o1.getDislikeCount() < o2.getDislikeCount()) {
                                return 1;
                            } else if (o1.getDislikeCount() == o2.getDislikeCount()) {
                                return 0;
                            } else {
                                return -1;
                            }
                        })
                        .reversed());
            }

        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<PostDto> getPosts() {
            return posts;
        }

        public void setPosts(List<PostDto> posts) {
            this.posts = posts;
        }
    }

    private static class PostDto {
        private int id;
        private long timestamp;
        private UserDto user;
        private String title;
        private String announce;
        private String text;
        private long likeCount = 0;
        private long dislikeCount = 0;
        private int commentCount = 0;
        private int viewCount;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<CommentDto> comments;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<String> tags;

        public PostDto(Post post) {
            id = post.getId();
            timestamp = dateToLong(post.getTime());
            user = new UserDto(
                    post.getUser().getId(),
                    post.getUser().getName());
            title = post.getTitle();
            announce = post.getText()
                    .substring(0, Math.min(post.getText().length(), 100))
                    .replaceAll("\\<[^>]*>", "") + "...";
            text = post.getText();
            viewCount = post.getViewCount();

            String hql;
            try (Session session = Connection.getSession()) {
                Transaction transaction = session.beginTransaction();

                hql = "select t.name from TagToPost tp"
                        + " join Tag t on tp.tag.id = t.id"
                        + " where tp.post.id=" + id
                ;
                tags = session.createQuery(hql).getResultList();

                hql = "select value, count(*) from PostVote where post.id=" + id
                        + " group by value";
                List<Object[]> rows = session.createQuery(hql).getResultList();
                for (Object[] row : rows) {
                    int value = (Integer) row[0];
                    long count = (Long) row[1];
                    if (value == -1) {
                        dislikeCount = count;
                    }
                    if (value == 1) {
                        likeCount = count;
                    }
                }

                hql = "from PostComment where post.id= " + id;
                comments = session.createQuery(hql).getResultList();
                commentCount = comments.size();

                transaction.commit();
            }

        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public UserDto getUser() {
            return user;
        }

        public void setUser(UserDto user) {
            this.user = user;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAnnounce() {
            return announce;
        }

        public void setAnnounce(String announce) {
            this.announce = announce;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public long getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public long getDislikeCount() {
            return dislikeCount;
        }

        public void setDislikeCount(int dislikeCount) {
            this.dislikeCount = dislikeCount;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public int getViewCount() {
            return viewCount;
        }

        public void setViewCount(int viewCount) {
            this.viewCount = viewCount;
        }

        public List<CommentDto> getComments() {
            return comments;
        }

        public void setComments(List<CommentDto> comments) {
            this.comments = comments;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }
    }

    private static class UserDto {
        private int id;
        private String name;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String photo = "/img/avatars/avatar.jpg";

        public UserDto(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }

    private static class CalendarDto {
        private List<String> years;
        private Map<String, Long> posts;

        public CalendarDto(List<String> years, Map<String, Long> posts) {
            this.years = years;
            this.posts = posts;
        }

        public List<String> getYears() {
            return years;
        }

        public void setYears(List<String> years) {
            this.years = years;
        }

        public Map<String, Long> getPosts() {
            return posts;
        }

        public void setPosts(Map<String, Long> posts) {
            this.posts = posts;
        }
    }

    private static class CommentDto {
        private int id;
        private long timestamp;
        private String text;
        private UserDto user;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public UserDto getUser() {
            return user;
        }

        public void setUser(UserDto user) {
            this.user = user;
        }

    }

    private enum SortDescription {
        RECENT,
        POPULAR,
        BEST,
        EARLY,
        DEFAULT;

    }
}

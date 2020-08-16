package diploma.controller;

import diploma.main.Connection;
import diploma.model.Post;
import diploma.model.PostComment;
import diploma.model.PostVote;
import diploma.model.TagToPostRelation;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@RestController
public class ApiPostController {

    public static final String basePostCondition = "is_active=1 AND moderation_status='ACCEPTED' AND time < NOW()";

    @GetMapping("/api/post")
    public PostResponse getPostResponse(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("mode") String mode
    ) {
        return new PostResponse("", offset, limit, convertToSort(mode));

    }

    @GetMapping("/api/post/{id}")
    public PostIdResponse getPostIdResponse(@PathVariable int id) {
        return new PostIdResponse();
    }

    @GetMapping("/api/post/search")
    public PostResponse getSearchResponse(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("query") String searchText
    ) {

        String selectCondition = "text like '%:text%'"
                + " OR title like '%:text%'"
                + " OR id IN (select pc.post.id from PostComment pc where pc.text like '%:text%')"
                + " OR user_id IN (select u.id from User u where u.name like '%:text%')"
                + " OR id IN (select ttp.post.id from TagToPostRelation ttp where ttp.tag.id"
                + " in (select t.id from Tag t where t.name like '%:text%'))";
        selectCondition = selectCondition.replaceAll(":text", searchText);

        return new PostResponse(selectCondition, offset, limit);
    }

    @GetMapping("/api/post/byTag")
    public PostResponse getByTagResponse(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("tag") String tag
    ) {

        String byTagCondition = "id in (select ttp.post.id from TagToPostRelation ttp where ttp.tag.id = " +
                "(select t.id from Tag t where t.name = '" + tag + "'))";

        return new PostResponse(byTagCondition, offset, limit);

    }

    @GetMapping("/api/pos/byDate")
    public PostResponse getByDateResponse(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("date") String date
    ) {

        String byDateCondition = "date_format(time, '%Y-%m-%d') = " + date;

        return new PostResponse(byDateCondition, offset, limit);
    }

    @GetMapping("/api/calendar")
    public CalendarResponse getCalendarResponse(
            @RequestParam("year") int year
    ) {
        String yearsHql = "select date_format(time, '%Y') as year from Post group by year order by year desc";
        String postsHql = "select date_format(time, '%Y-%m-%d') as date, count(*) as count from Post"
                + " where year(time) = " + year
                + " group by date"
                + " order by date desc";
        List<String> years;
        List<Object> r;
        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            years = session.createQuery(yearsHql).getResultList();
            r = session.createQuery(postsHql).getResultList();

            transaction.commit();

        }

        return new CalendarResponse(years, r);
    }


    private static ResponsePost createResponsePost(Post post) {
        ResponsePost responsePost = new ResponsePost();
        responsePost.setId(post.getId());
        try {
            responsePost.setTimestamp(
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(
                            post.getTime().toString()
                    ).getTime() / 1000
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        responsePost.setUser(
                new ResponseUser(
                        post.getUser().getId(),
                        post.getUser().getName()
                ));
        responsePost.setTitle(post.getTitle());
        responsePost.setAnnounce(
                post.getText()
                        .substring(0, Math.min(post.getText().length(), 100))
                        .replaceAll("\\<[^>]*>", "") + "...");
        //ASK как получать кол-во лайков, дизлайков и комментов???
        responsePost.setLikeCount(0);
        responsePost.setDislikeCount(0);
        responsePost.setCommentCount(0);
        responsePost.setViewCount(post.getViewCount());
        return responsePost;
    }

    private static Sort convertToSort(String name) {
        Sort sort = Sort.DEFAULT;
        switch (name) {
            case "recent":
                sort = Sort.RECENT;
                break;
            case "early":
                sort = Sort.EARLY;
                break;
            case "popular":
                sort = Sort.POPULAR;
                break;
            case "best":
                sort = Sort.BEST;
                break;
        }
        return sort;
    }


    private static class PostResponse {
        private int count = 0;
        private List<ResponsePost> posts = new ArrayList<>();

        public PostResponse(String conditions, int offset, int limit) {
            this(conditions, offset, limit, Sort.DEFAULT);
        }

        public PostResponse(String conditions, int offset, int limit, Sort sort) {

            String baseHql = "FROM Post WHERE " + basePostCondition;

            String orderByTime = "";
            if (sort == Sort.EARLY) {
                orderByTime = " ORDER BY time";
            } else if (sort == Sort.RECENT) {
                orderByTime = " ORDER BY time DESC";
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

//                String selectedPostIdString = "";
//                for (Post post : postList) {
//                    selectedPostIdString += (selectedPostIdString.isEmpty()) ? post.getId() : ", " + post.getId();
//                }
//
//                //ASK от базы можно получить просто гтовую таблицу таблицу post_id+count, как получить это в в объект?
//                hql = "FROM " + PostVote.class.getSimpleName()
//                        + " WHERE post_id IN (:selectedPosts)";
//                postVoteList = session.createQuery(hql)
//                        .setParameter("selectedPosts", selectedPostIdString)
//                        .getResultList();
//
//                //ASK аналогичный вопрос, только тут post_id+like+dislike
//                hql = "FROM " + PostComment.class.getSimpleName()
//                        + " WHERE post_id IN (:selectedPosts)";
//                postCommentList = session.createQuery(hql)
//                        .setParameter("selectedPosts", selectedPostIdString)
//                        .getResultList();

                transaction.commit();
            }

            for (Post post : postList) {
                posts.add(createResponsePost(post));
            }

            if (sort == Sort.POPULAR) {
                posts.sort(Comparator.comparing(ResponsePost::getCommentCount)
                        .reversed());
            }
            if (sort == Sort.BEST) {
                posts.sort(Comparator.comparing(ResponsePost::getLikeCount)
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

        public List<ResponsePost> getPosts() {
            return posts;
        }

        public void setPosts(List<ResponsePost> posts) {
            this.posts = posts;
        }
    }

    private static class ResponsePost {
        private int id;
        private long timestamp;
        private ResponseUser user;
        private String title;
        private String announce;
        private int likeCount;
        private int dislikeCount;
        private int commentCount;
        private int viewCount;

        public ResponsePost() {
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

        public ResponseUser getUser() {
            return user;
        }

        public void setUser(ResponseUser user) {
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

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public int getDislikeCount() {
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
    }

    private static class ResponseUser {
        private int id;
        private String name;

        public ResponseUser(int id, String name) {
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
    }

    private static class CalendarResponse {
        private List<String> years;
        private List posts;

        public CalendarResponse(List<String> years, List posts) {
            this.years = years;
            this.posts = posts;
        }

        public List<String> getYears() {
            return years;
        }

        public void setYears(List<String> years) {
            this.years = years;
        }

        public List getPosts() {
            return posts;
        }

        public void setPosts(List posts) {
            this.posts = posts;
        }

    }

    private static class PostIdResponse {
        private int id = 4;
        private long timestamp = 1592338706;
        private boolean active = true;
        private ResponseUser user = new ResponseUser(1, "Света");
        private String title = "Коротко бла-бла-бла";
        private String text = "бла-бла-бла бла-бла-бла бла-бла-бла бла-бла-бла бла-бла-бла бла-бла-бла";
        private int likeCount = 10;
        private int dislikeCount = 7;
        private int viewCount = 28;
        private List<ResponseComment> comments = Arrays.asList(new ResponseComment(), new ResponseComment());
        private List<String> tags = Arrays.asList("первый", "второй");

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

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public ResponseUser getUser() {
            return user;
        }

        public void setUser(ResponseUser user) {
            this.user = user;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public int getDislikeCount() {
            return dislikeCount;
        }

        public void setDislikeCount(int dislikeCount) {
            this.dislikeCount = dislikeCount;
        }

        public int getViewCount() {
            return viewCount;
        }

        public void setViewCount(int viewCount) {
            this.viewCount = viewCount;
        }

        public List<ResponseComment> getComments() {
            return comments;
        }

        public void setComments(List<ResponseComment> comments) {
            this.comments = comments;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }
    }

    private static class ResponseComment {
        private int id = (int) (Math.random() * 10);
        private long timestamp = 1592338706;
        private String text = "бла-бла два раза";
        private ResponseUserWithAvatar user = new ResponseUserWithAvatar();

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

        public ResponseUserWithAvatar getUser() {
            return user;
        }

        public void setUser(ResponseUserWithAvatar user) {
            this.user = user;
        }
    }

    private static class ResponseUserWithAvatar {
        private int id = (int) (Math.random()*10);
        private String name = "Человек " + id;
        private String photo;

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


    private enum Sort {
        RECENT,
        POPULAR,
        BEST,
        EARLY,
        DEFAULT
    }

}

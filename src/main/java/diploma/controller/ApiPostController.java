package diploma.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import diploma.main.Connection;
import diploma.model.Post;
import diploma.model.PostComment;
import diploma.model.PostVote;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
public class ApiPostController {

    private static final String baseCondition = "is_active=1 AND moderation_status='ACCEPTED' AND time < NOW()";

    @GetMapping("/api/post")
    public PostListDto getPostList(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("mode") String mode
    ) {
        return new PostListDto("", offset, limit, getSort(mode));

    }

    @GetMapping("/api/post/{id}")
    public PostByIdDto getPostById(@PathVariable int id) {
        return new PostByIdDto();
    }

    @GetMapping("/api/post/search")
    public PostListDto getSearchList(
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

        return new PostListDto(selectCondition, offset, limit);
    }

    @GetMapping("/api/post/byTag")
    public PostListDto getByTag(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("tag") String tag
    ) {

        String byTagCondition = "id in (select ttp.post.id from TagToPostRelation ttp where ttp.tag.id = " +
                "(select t.id from Tag t where t.name = '" + tag + "'))";

        return new PostListDto(byTagCondition, offset, limit);

    }

    @GetMapping("/api/post/byDate")
    public PostListDto getByDate(
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("date") String date
    ) {

        //FIXME запрос НЕ РАБОТАЕТ!!!
        String byDateCondition = "date_format(time, '%Y-%m-%d') = " + date;

        return new PostListDto(byDateCondition, offset, limit);
    }

    @GetMapping("/api/calendar")
    public CalendarDto getCalendar(
            @RequestParam("year") int year
    ) {
        String yearsHql = "select date_format(time, '%Y') as year from Post group by year order by year desc";
        String postsHql = "select date_format(time, '%Y-%m-%d') as date, count(*) as count from Post"
                + " where year(time) = " + year
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

        return new CalendarDto(years, postsInDay);
    }


    private static PostDto createPostDto(Post post, int likeCount, int dislikeCount, int commentCount) {
        //FIXME убрать в класс PostDto
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        try {
            postDto.setTimestamp(
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(
                            post.getTime().toString()
                    ).getTime() / 1000
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        postDto.setUser(
                new UserDto(
                        post.getUser().getId(),
                        post.getUser().getName()
                ));
        postDto.setTitle(post.getTitle());
        postDto.setAnnounce(
                post.getText()
                        .substring(0, Math.min(post.getText().length(), 100))
                        .replaceAll("\\<[^>]*>", "") + "...");
        postDto.setLikeCount(likeCount);
        postDto.setDislikeCount(dislikeCount);
        postDto.setCommentCount(commentCount);
        postDto.setViewCount(post.getViewCount());
        return postDto;
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


    private static class PostListDto {
        private int count;
        private List<PostDto> posts = new ArrayList<>();

        public PostListDto(String conditions, int offset, int limit) {
            this(conditions, offset, limit, SortDescription.DEFAULT);
        }

        public PostListDto(String conditions, int offset, int limit, SortDescription sort) {

            String baseHql = "FROM Post WHERE " + baseCondition;

            String orderByTime = "";
            if (sort == SortDescription.EARLY) {
                orderByTime = " ORDER BY time";
            } else if (sort == SortDescription.RECENT) {
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

                for (Post post : postList) {

                    //FIXME Исправить костыль!!!
                    //ИМХО КОСТЫЛЬ вытаскивать лайки и комменты отдельными запросами
                    int postId = post.getId();

                    hql = "FROM " + PostVote.class.getSimpleName()
                            + " WHERE post_id = :id";
                    List<PostVote> postVoteList = session.createQuery(hql)
                            .setParameter("id", postId)
                            .getResultList();
                    int likeCount = (int) postVoteList.stream()
                            .filter(postVote -> postVote.getValue() == 1)
                            .count();
                    int dislikeCount = (int) postVoteList.stream()
                            .filter(postVote -> postVote.getValue() == -1)
                            .count();

                    hql = "FROM " + PostComment.class.getSimpleName()
                            + " WHERE post_id = :id";
                    int commentCount = (int) session.createQuery(hql)
                            .setParameter("id", postId)
                            .getResultStream().count();

                    //КОНЕЦ КОСТЫЛЯ

                    posts.add(createPostDto(post, likeCount, dislikeCount, commentCount));
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
        private int likeCount;
        private int dislikeCount;
        private int commentCount;
        private int viewCount;

        public PostDto() {
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

    private static class PostByIdDto {
        //FIXME исправить после удаления костыля
        private int id = 4;
        private long timestamp = 1592338706;
        private boolean active = true;
        private UserDto user = new UserDto(1, "Света");
        private String title = "Коротко бла-бла-бла";
        private String text = "бла-бла-бла бла-бла-бла бла-бла-бла бла-бла-бла бла-бла-бла бла-бла-бла";
        private int likeCount = 10;
        private int dislikeCount = 7;
        private int viewCount = 28;
        private List<CommentDto> comments = Arrays.asList(new CommentDto(), new CommentDto());
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

    private static class CommentDto {
        private int id = (int) (Math.random() * 10);
        private long timestamp = 1592338706;
        private String text = "бла-бла два раза";
        private UserDto user = new UserDto(1, "First");

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
        DEFAULT
    }

}

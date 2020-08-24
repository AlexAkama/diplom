package diploma.main;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import diploma.model.Post;
import diploma.model.PostComment;
import diploma.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.Timestamp;
import java.util.List;
import java.util.*;

import static diploma.model.GlobalSettingsValue.getSetValue;

public class Dto {

    public static final String baseCondition = "p.isActive=1 AND p.moderationStatus='ACCEPTED' AND p.time < NOW()";

    public static String dateToSqlDate(Date date) {
        return new java.sql.Timestamp(date.getTime()).toString();
    }

    public static long dateToTimestamp(Date date) {
        return date.getTime() / 1000;
    }

    public static String randomString(int length) {
        char[] chars = "ACEFGHJKLMNPQRUVWXYabcdefhijkprstuvwx1234567890".toCharArray();
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(chars[random.nextInt(chars.length)]);
        }
        return stringBuilder.toString();
    }

    public static List<String> getTagsList(int postId) {
        List<String> tags;
        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            String hql = "select t.name from TagToPost tp"
                    + " join Tag t on tp.tag.id = t.id"
                    + " where tp.post.id=:id";
            tags = session.createQuery(hql).setParameter("id", postId).getResultList();

            transaction.commit();
        }
        return tags;
    }

    public static List<CommentDto> getCommentsList(int postId) {
        List<CommentDto> comments = new ArrayList<>();
        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            String hql = "from PostComment where post.id=:id";
            List<PostComment> resultList = session.createQuery(hql).setParameter("id", postId).getResultList();

            for (PostComment comment : resultList) {
                comments.add(new CommentDto().make(comment));
            }

            transaction.commit();
        }
        return comments;
    }

    public static BufferedImage resizeForCaptcha(BufferedImage image) {
        return createResizedImage(image, 100, 35);
    }

    private static BufferedImage createResizedImage(BufferedImage original, int width, int heigth) {
        BufferedImage result = new BufferedImage(width, heigth, original.getType());
        Graphics2D graphics2D = result.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(original, 0, 0, width, heigth, null);
        graphics2D.dispose();
        return result;
    }

    public static void saveGlobalParameter(String name, boolean value) {
        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            String hql = "update GlobalSetting set value=:value where code=:code";
            session.createQuery(hql)
                    .setParameter("value", getSetValue(value))
                    .setParameter("code", name)
                    .executeUpdate();

            transaction.commit();
        }
    }


    @JsonPropertyOrder({"id", "timestamp", "active", "user", "title", "announce", "text",
            "likeCount", "dislikeCount", "commentCount", "viewCount", "comments", "tags"})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PostDto {
        private int id;
        private long timestamp;
        private boolean active;
        private UserDto user;
        private String title;
        private String announce;
        private String text;
        private long likeCount;
        private long dislikeCount;
        private int commentCount;
        private int viewCount;
        private List<CommentDto> comments;
        private List<String> tags;

        public PostDto make(Post post) {
            return makeDto(post, false);
        }

        public PostDto makeAnnounce(Post post) {
            return makeDto(post, true);
        }

        private PostDto makeDto(Post post, boolean isAnnounce) {
            id = post.getId();
            timestamp = dateToTimestamp(post.getTime());
            user = new UserDto(
                    post.getUser().getId(),
                    post.getUser().getName());
            title = post.getTitle();
            if (isAnnounce) {
                announce = post.getText()
                        .substring(0, Math.min(post.getText().length(), 100))
                        .replaceAll("<[^>]*>", "") + "...";
            } else {
                active = post.isActive();
                System.out.println(">>>" + active);
                text = post.getText();
                tags = getTagsList(id);
                comments = getCommentsList(id);
                commentCount = comments.size();
            }
            viewCount = post.getViewCount();

            LikesDto likesDto = new LikesDto().getPostResult(id);
            likeCount = likesDto.getLikeCount();
            dislikeCount = likesDto.getDislikeCount();

            return this;
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

    public static class PostListDto {

        private long count = 0;
        private List<PostDto> posts = new ArrayList<>();

        public PostListDto makeAnnounces(int offset, int limit, PostViewMode sort) {
            return getResult(true, "", offset, limit, sort);
        }

        public PostListDto makeAnnounces(String conditions, int offset, int limit) {
            return getResult(true, conditions, offset, limit, PostViewMode.NONE);
        }

        public PostListDto makeAnnounces(String conditions, int offset, int limit, PostViewMode sort) {
            return getResult(true, conditions, offset, limit, sort);
        }

        private PostListDto getResult(Boolean useBaseCondition, String conditions, int offset, int limit, PostViewMode mode) {

            //FIXME облагородить HQL
            String hql = "from Post p where ";
            String countHql = "select count(*) from Post p where ";
            if (useBaseCondition) {
                hql += baseCondition;
                countHql += baseCondition;
            }
            if (!conditions.isEmpty()) {
                hql += " and (" + conditions + ")";
                countHql += " and (" + conditions + ")";
            }

            String orderByTime = "";
            switch (mode) {
                case EARLY:
                    orderByTime = " ORDER BY p.time";
                    break;
                case RECENT:
                    orderByTime = " ORDER BY p.time DESC";
                    break;
            }

            List<Post> postList;
            try (Session session = Connection.getSession()) {
                Transaction transaction = session.beginTransaction();

                count = (long) session.createQuery(countHql).uniqueResult();

                hql += orderByTime;
                postList = session.createQuery(hql)
                        .setFirstResult(offset)
                        .setMaxResults(limit)
                        .getResultList();

                for (Post post : postList) {
                    posts.add(new PostDto().makeAnnounce(post));
                }

                transaction.commit();
            }

            switch (mode) {
                case POPULAR:
                    posts.sort(Comparator.comparing(PostDto::getCommentCount)
                            .reversed());
                    break;
                case BEST:
                    posts.sort(Comparator.comparing(PostDto::getLikeCount)
                            .thenComparing((o1, o2) -> Long.compare(o2.getDislikeCount(), o1.getDislikeCount()))
                            .reversed());
                    break;
            }

            return this;
        }

        public long getCount() {
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

    public static class UserDto {
        private int id;
        private String name;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String photo;

        public UserDto(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public UserDto(int id, String name, String photo) {
            this.id = id;
            this.name = name;
            this.photo = photo;
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

    @JsonPropertyOrder({"id", "timestamp", "text", "user"})
    public static class CommentDto {
        private int id;
        private long timestamp;
        private String text;
        private UserDto user;

        public CommentDto make(PostComment comment) {
            id = comment.getId();
            timestamp = dateToTimestamp(comment.getTime());
            text = comment.getText();
            user = new UserDto(
                    comment.getUser().getId(),
                    comment.getUser().getName(),
                    comment.getUser().getPhoto()
            );
            return this;
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

    public static class LikesDto {
        private long likeCount;
        private long dislikeCount;

        public LikesDto getBlogResult() {
            return getResult(0, 0);
        }

        public LikesDto getUserResult(int id) {
            return getResult(0, id);
        }

        public LikesDto getPostResult(int id) {
            return getResult(id, 0);
        }

        private LikesDto getResult(int postId, int userId) {
            try (Session session = Connection.getSession()) {
                Transaction transaction = session.beginTransaction();

                String whereByPostId = (postId != 0 && userId == 0) ? " where post.id=" + postId : "";
                String whereByUserId = (userId != 0 && postId == 0) ? " where user.id=" + userId : "";
                String hql = " select" +
                        " count(case when value=1 then 1 else null end) as likeCount, " +
                        " count(case when value=-1 then 1 else null end) as dislikeCount" +
                        " from PostVote" + whereByPostId + whereByUserId;
                Object[] row = (Object[]) session.createQuery(hql).uniqueResult();
                likeCount = (long) row[0];
                dislikeCount = (long) row[1];

                transaction.commit();
            }
            return this;

        }

        public LikesDto() {
            likeCount = 0;
            dislikeCount = 0;
        }

        public long getLikeCount() {
            return likeCount;
        }

        public long getDislikeCount() {
            return dislikeCount;
        }
    }

    public static class StatDto {
        private long postsCount;
        private long viewsCount;
        private long firstPublication;


        public StatDto getBlogResult() {
            return getResult(0);
        }

        public StatDto getUserResult(int id) {
            return getResult(id);
        }

        private StatDto getResult(int userId) {
            try (Session session = Connection.getSession()) {
                Transaction transaction = session.beginTransaction();

                String whereByUserId = (userId != 0) ? " where user.id=" + userId : "";
                String hql = "select count(*), sum(viewCount), min(time) from Post" + whereByUserId;
                Object[] row = (Object[]) session.createQuery(hql).uniqueResult();
                postsCount = (long) row[0];
                viewsCount = (long) row[1];
                firstPublication = ((Timestamp) row[2]).getTime() / 1000;

                transaction.commit();
            }
            return this;
        }

        public long getPostsCount() {
            return postsCount;
        }

        public long getViewsCount() {
            return viewsCount;
        }

        public long getFirstPublication() {
            return firstPublication;
        }
    }

    public static class TagDto {
        private String name;
        private double weight;

        public TagDto(String name, double weight) {
            this.name = name;
            this.weight = weight;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }
    }

    public static class TagListDto {
        List<TagDto> tags;

        public TagListDto(List<TagDto> tags) {
            this.tags = tags;
        }

        public List<TagDto> getTags() {
            return tags;
        }

        public void setTags(List<TagDto> tags) {
            this.tags = tags;
        }
    }

    public static class CalendarDto {
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

    public static class ResultDto {
        private final boolean result;

        public ResultDto(boolean result) {
            this.result = result;
        }

        public boolean isResult() {
            return result;
        }
    }

    public static class CaptchaDto {
        private String secret;
        private String image;

        public CaptchaDto(String secret, String image) {
            this.secret = secret;
            this.image = image;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    public static class LoginDto {
        private boolean result;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private AuthUserDto user;

        public LoginDto() {
            result = false;
            user = null;
        }

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public AuthUserDto getUser() {
            return user;
        }

        public void setUser(AuthUserDto user) {
            this.user = user;
        }

    }

    public static class AuthUserDto {
        private int id;
        private String name;
        private String photo;
        private String email;
        private boolean moderation = false;
        private long moderationCount = 0;
        private boolean settings = false;

        public AuthUserDto(User user) {
            id = user.getId();
            name = user.getName();
            photo = user.getPhoto();
            email = user.getEmail();
            if (user.isModerator()) {
                moderation = true;
                settings = true;
                try (Session session = Connection.getSession()) {
                    Transaction transaction = session.beginTransaction();

                    String hql = "select count(*) from Post p where p.moderationStatus = 'NEW'";
                    moderationCount = (long) session.createQuery(hql).uniqueResult();

                    transaction.commit();
                }
            }
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public boolean isModeration() {
            return moderation;
        }

        public void setModeration(boolean moderation) {
            this.moderation = moderation;
        }

        public long getModerationCount() {
            return moderationCount;
        }

        public void setModerationCount(long moderationCount) {
            this.moderationCount = moderationCount;
        }

        public boolean isSettings() {
            return settings;
        }

        public void setSettings(boolean settings) {
            this.settings = settings;
        }
    }

    public static class LoginRequest {

        @JsonProperty("e_mail")
        private String email;
        private String password;

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class RegistrationRequest {
        @JsonProperty("e_mail")
        private String email;
        private String password;
        private String name;
        @JsonProperty("captcha")
        private String code;
        @JsonProperty("captcha_secret")
        private String secret;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }
    }

    public static class RegisterDto {
        private boolean result;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private Map<String, String> errors;

        public boolean isResult() {
            return result;
        }

        public RegisterDto(boolean result, Map<String, String> errors) {
            this.result = result;
            this.errors = errors;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public Map<String, String> getErrors() {
            return errors;
        }

        public void setErrors(Map<String, String> errors) {
            this.errors = errors;
        }
    }

    @JsonPropertyOrder()
    public static class GlobalSettingsDto {
        @JsonProperty("MULTIUSER_MODE")
        private boolean multiUser;
        @JsonProperty("POST_PREMODERATION")
        private boolean preModeration;
        @JsonProperty("STATISTICS_IS_PUBLIC")
        private boolean publicStatistic;

        public GlobalSettingsDto(boolean multiUser, boolean preModeration, boolean publicStatistic) {
            this.multiUser = multiUser;
            this.preModeration = preModeration;
            this.publicStatistic = publicStatistic;
        }

        public boolean isMultiUser() {
            return multiUser;
        }

        public void setMultiUser(boolean multiUser) {
            this.multiUser = multiUser;
        }

        public boolean isPreModeration() {
            return preModeration;
        }

        public void setPreModeration(boolean preModeration) {
            this.preModeration = preModeration;
        }

        public boolean isPublicStatistic() {
            return publicStatistic;
        }

        public void setPublicStatistic(boolean publicStatistic) {
            this.publicStatistic = publicStatistic;
        }
    }


    public enum PostViewMode {
        RECENT,
        POPULAR,
        BEST,
        EARLY,
        NONE
    }

    public enum PostState {
        INACTIVE,
        PENDING,
        DECLINED,
        PUBLISHED
    }
}

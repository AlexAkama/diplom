package project.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import project.dto.main.UserDto;
import project.model.Post;

import java.util.List;

@JsonPropertyOrder({"id", "timestamp", "active", "user", "title", "announce", "text",
        "likeCount", "dislikeCount", "commentCount", "viewCount", "comments", "tags"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDto {
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
        timestamp = Dto.dateToTimestamp(post.getTime());
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
            tags = Dto.getTagsList(id);
            comments = Dto.getCommentsList(id);
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

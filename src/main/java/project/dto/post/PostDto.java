package project.dto.post;

import com.fasterxml.jackson.annotation.*;
import project.dto.CommentDto;
import project.dto.UserDto;

import java.util.List;

@JsonPropertyOrder({"id", "timestamp", "active", "user", "title", "announce", "text",
        "likeCount", "dislikeCount", "commentCount", "viewCount", "comments", "tags"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PostDto {

    private long id;
    private long timestamp;
    private Boolean active ;
    private UserDto user;
    private String title;
    private String announce;
    private String text = null;
    @JsonProperty("likeCount")
    private long likeCounter;
    @JsonProperty("dislikeCount")
    private long dislikeCounter;
    @JsonProperty("commentCount")
    private Integer commentCounter;
    @JsonProperty("viewCount")
    private int viewCounter;
    private List<CommentDto> commentList;
    private List<String> tagList;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
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

    public long getLikeCounter() {
        return likeCounter;
    }

    public void setLikeCounter(long likeCounter) {
        this.likeCounter = likeCounter;
    }

    public long getDislikeCounter() {
        return dislikeCounter;
    }

    public void setDislikeCounter(long dislikeCounter) {
        this.dislikeCounter = dislikeCounter;
    }

    public Integer getCommentCounter() {
        return commentCounter;
    }

    public void setCommentCounter(Integer commentCounter) {
        this.commentCounter = commentCounter;
    }

    public int getViewCounter() {
        return viewCounter;
    }

    public void setViewCounter(int viewCounter) {
        this.viewCounter = viewCounter;
    }

    public List<CommentDto> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentDto> commentList) {
        this.commentList = commentList;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

}

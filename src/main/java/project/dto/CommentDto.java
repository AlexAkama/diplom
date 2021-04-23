package project.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import project.config.AppConstant;
import project.model.PostComment;

@JsonPropertyOrder({"id", "timestamp", "text", "user"})
public class CommentDto {
    private long id;
    private long timestamp;
    private String text;
    private UserDto user;

    public CommentDto (PostComment comment) {
        id = comment.getId();
        timestamp = AppConstant.dateToTimestamp(comment.getTime());
        text = comment.getText();
        user = new UserDto(
                comment.getUser().getId(),
                comment.getUser().getName(),
                comment.getUser().getPhoto()
        );
    }

    public long getId() {
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

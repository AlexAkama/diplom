package diploma.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import diploma.model.PostComment;

@JsonPropertyOrder({"id", "timestamp", "text", "user"})
public class CommentDto {
    private int id;
    private long timestamp;
    private String text;
    private UserDto user;

    public CommentDto createFrom(PostComment comment) {
        id = comment.getId();
        timestamp = Dto.dateToTimestamp(comment.getTime());
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

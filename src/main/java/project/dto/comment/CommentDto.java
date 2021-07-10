package project.dto.comment;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import project.config.AppConstant;
import project.dto.UserDto;
import project.model.PostComment;

@JsonPropertyOrder({"id", "timestamp", "text", "user"})
@Getter
@Setter
public class CommentDto {

    private long id;
    private long timestamp;
    private String text;
    private UserDto user;

    public CommentDto(PostComment comment) {
        id = comment.getId();
        timestamp = AppConstant.dateToTimestamp(comment.getTime());
        text = comment.getText();
        user = new UserDto(
                comment.getUser().getId(),
                comment.getUser().getName(),
                comment.getUser().getPhoto()
        );
    }


}

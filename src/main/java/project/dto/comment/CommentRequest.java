package project.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {

    @JsonProperty("parent_id")
    private long parentId;
    @JsonProperty("post_id")
    private long postId;
    private String text;

}

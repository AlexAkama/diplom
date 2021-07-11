package project.dto.moderation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModerationRequest {

    @JsonProperty("post_id")
    private long postId;
    private String decision;

}

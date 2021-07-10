package project.dto.vote;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteRequest {
    @JsonProperty("post_id")
    private long postId;

}

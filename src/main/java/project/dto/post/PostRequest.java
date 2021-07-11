package project.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest {

    private long timestamp;
    private boolean active;
    private String title;
    @JsonProperty("tags")
    private String[] tagArray;
    private String text;

}

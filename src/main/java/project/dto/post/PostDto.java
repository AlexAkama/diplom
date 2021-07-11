package project.dto.post;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;
import project.dto.UserDto;
import project.dto.comment.CommentDto;

import java.util.List;

@JsonPropertyOrder({"id", "timestamp", "active", "user", "title", "announce", "text",
        "likeCount", "dislikeCount", "commentCount", "viewCount", "comments", "tags"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@Setter
public class PostDto {

    private long id;
    private long timestamp;
    private boolean active;
    private UserDto user;
    private String title;
    private String announce;
    private String text;
    @JsonProperty("likeCount")
    private long likeCounter;
    @JsonProperty("dislikeCount")
    private long dislikeCounter;
    @JsonProperty("commentCount")
    private long commentCounter;
    @JsonProperty("viewCount")
    private long viewCounter;
    @JsonProperty("comments")
    private List<CommentDto> commentList;
    @JsonProperty("tags")
    private List<String> tagList;

}

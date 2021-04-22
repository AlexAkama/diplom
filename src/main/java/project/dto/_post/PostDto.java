package project.dto._post;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import project.dto.*;
import project.model.Post;
import project.model.emun.PostDtoStatus;

import java.util.List;

import static project.model.emun.PostDtoStatus.*;

@JsonPropertyOrder({"id", "timestamp", "active", "user", "title", "announce", "text",
        "likeCount", "dislikeCount", "commentCount", "viewCount", "comments", "tags"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDto {

    private final long id;
    private final long timestamp;
    private final Boolean active;
    private final UserDto user;
    private final String title;
    private final String announce;
    private final String text;
    private final long likeCounter;
    private final long dislikeCounter;
    private final Integer commentCounter;
    private final int viewCounter;
    private final List<CommentDto> commentList;
    private final List<String> tagList;



    private PostDto(Post post, VoteCounterDto voteCounterDto, PostDtoStatus status) {
        id = post.getId();
        timestamp = Dto.dateToTimestamp(post.getTime());
        user = new UserDto(
                post.getUser().getId(),
                post.getUser().getName());
        title = post.getTitle();
        if (status == ANNOUNCE) {
            active = null;
            announce = post.getText()
                    .substring(0, Math.min(post.getText().length(), 100))
                    .replaceAll("<[^>]*>", "") + "...";
            text = null;
            tagList = null;
            commentList = null;
            commentCounter = null;
        } else {
            active = post.isActive();
            announce = null;
            text = post.getText();
            tagList = Dto.getTagsList(id);
            commentList = Dto.getCommentsList(id);
            commentCounter = commentList.size();
        }
        viewCounter = post.getViewCount();

//        VoteCounterDto voteCounterDto = new VoteCounterDto().getPostResult(id);
        likeCounter = voteCounterDto.getLikeCounter();
        dislikeCounter = voteCounterDto.getDislikeCounter();

    }

    public long getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isActive() {
        return active;
    }

    public UserDto getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getAnnounce() {
        return announce;
    }

    public String getText() {
        return text;
    }

    public long getLikeCounter() {
        return likeCounter;
    }

    public long getDislikeCounter() {
        return dislikeCounter;
    }

    public int getCommentCounter() {
        return commentCounter;
    }

    public int getViewCounter() {
        return viewCounter;
    }

    public List<CommentDto> getCommentList() {
        return commentList;
    }

    public List<String> getTagList() {
        return tagList;
    }

}

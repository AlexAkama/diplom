package project.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentRequest {

    @JsonProperty("parent_id")
    private long parentId;
    @JsonProperty("post_id")
    private long postId;
    private String text;


    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}

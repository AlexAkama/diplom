package project.dto.vote;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VoteRequest {
    @JsonProperty("post_id")
    private long postId;

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

}

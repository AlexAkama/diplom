package project.dto.moderation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModerationRequest {

    @JsonProperty("post_id")
    private long postId;
    public String decision;


    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

}

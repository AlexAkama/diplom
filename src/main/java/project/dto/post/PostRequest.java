package project.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostRequest {

    private long timestamp;
    private boolean active;
    private String title;
    @JsonProperty("tags")
    private String[] tagArray;
    private String text;


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getTagArray() {
        return tagArray;
    }

    public void setTagArray(String[] tagArray) {
        this.tagArray = tagArray;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}

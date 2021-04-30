package project.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostAddRequest {

    long timestamp;
    boolean active;
    String title;
    @JsonProperty("tags")
    String[] tagArray;
    String text;


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

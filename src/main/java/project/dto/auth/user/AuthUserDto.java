package project.dto.auth.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthUserDto {
    private long id;
    private String name;
    private String photo;
    private String email;
    private boolean moderation;
    @JsonProperty("moderationCount")
    private long moderationCounter;
    private boolean settings;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isModeration() {
        return moderation;
    }

    public void setModeration(boolean moderation) {
        this.moderation = moderation;
    }

    public long getModerationCounter() {
        return moderationCounter;
    }

    public void setModerationCounter(long moderationCounter) {
        this.moderationCounter = moderationCounter;
    }

    public boolean isSettings() {
        return settings;
    }

    public void setSettings(boolean settings) {
        this.settings = settings;
    }
}

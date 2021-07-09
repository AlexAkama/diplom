package project.model;

import org.hibernate.annotations.Type;
import project.model.enums.ModerationStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "is_active", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean active;

    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_status", length = 8, columnDefinition = "varchar(8) default 'NEW'", nullable = false)
    private ModerationStatus moderationStatus;

    @OneToOne()
    private User moderator;

    @ManyToOne(optional = false)
    private User user;

    @Column(nullable = false)
    private Date time;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String text;

    @Column(name = "view_count")
    private long viewCounter;

    @Column(name = "like_count")
    private long likeCounter;

    @Column(name = "dislike_count")
    private long dislikeCounter;

    @Column(name = "comment_count")
    private long commentCounter;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ModerationStatus getModerationStatus() {
        return moderationStatus;
    }

    public void setModerationStatus(ModerationStatus moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public User getModerator() {
        return moderator;
    }

    public void setModerator(User moderator) {
        this.moderator = moderator;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getViewCounter() {
        return viewCounter;
    }

    public void setViewCounter(long viewCount) {
        this.viewCounter = viewCount;
    }

    public long getLikeCounter() {
        return likeCounter;
    }

    public void setLikeCounter(long likeCounter) {
        this.likeCounter = likeCounter;
    }

    public long getDislikeCounter() {
        return dislikeCounter;
    }

    public void setDislikeCounter(long dislikeCounter) {
        this.dislikeCounter = dislikeCounter;
    }

    public long getCommentCounter() {
        return commentCounter;
    }

    public void setCommentCounter(long commentCounter) {
        this.commentCounter = commentCounter;
    }

}

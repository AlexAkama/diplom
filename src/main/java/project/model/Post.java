package project.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import project.model.enums.ModerationStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "posts")
@Getter
@Setter
public class Post extends Identified {

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
    @Type(type = "text")
    private String text;

    @Column(name = "view_count")
    private long viewCounter;

    @Column(name = "like_count")
    private long likeCounter;

    @Column(name = "dislike_count")
    private long dislikeCounter;

    @Column(name = "comment_count")
    private long commentCounter;

}

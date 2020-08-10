package model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "is_active", nullable = false)
    @Type(type = "org.hibernate.type.ByteType")
    private boolean isActive;

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

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    public Post() {
    }

}

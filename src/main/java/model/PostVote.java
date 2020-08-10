package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "post_votes")
public class PostVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Post post;

    @Column(nullable = false)
    private Date time;

    @Column(nullable = false)
    private int value;

    public PostVote() {
    }
}

package project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "post_votes")
@Getter
@Setter
public class PostVote extends Identified {

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Post post;

    @Column(nullable = false)
    private Date time;

    @Column(nullable = false)
    private long value;

}

package project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "post_comments")
@Getter
@Setter
public class PostComment extends Identified {

    @OneToOne
    private PostComment parent;

    @ManyToOne(optional = false)
    private Post post;

    @ManyToOne(optional = false)
    private User user;

    @Column(nullable = false)
    private Date time;

    @Column(nullable = false)
    private String text;

}

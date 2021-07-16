package project.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "post_comments")
@Getter
@Setter
public class PostComment extends Identified {

    @ManyToOne
    private PostComment parent;

    @ManyToOne(optional = false)
    private Post post;

    @ManyToOne(optional = false)
    private User user;

    @Column(nullable = false)
    private Date time;

    @Column(nullable = false)
    @Type(type = "text")
    private String text;

}

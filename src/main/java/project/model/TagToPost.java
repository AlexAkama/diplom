package project.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tag2post")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TagToPost extends Identified {

    @OneToOne(optional = false)
    private Tag tag;

    @OneToOne(optional = false)
    private Post post;

}

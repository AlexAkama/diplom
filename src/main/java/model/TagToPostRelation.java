package model;

import javax.persistence.*;

@Entity
@Table(name = "tag2post")
public class TagToPostRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(optional = false)
    private Tag tag;

    @OneToOne(optional = false)
    private Post post;

    public TagToPostRelation() {
    }
}

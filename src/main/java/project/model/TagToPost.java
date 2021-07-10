package project.model;

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

    public TagToPost() {
    }

    public TagToPost(Tag tag, Post post) {
        this.tag = tag;
        this.post = post;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}

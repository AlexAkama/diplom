package project.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tags")
@NoArgsConstructor
@Getter
@Setter
public class Tag extends Identified {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean active;

    public Tag(String name) {
        this.name = name;
    }

}

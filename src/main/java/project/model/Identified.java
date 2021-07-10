package project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@MappedSuperclass
@Getter
@Setter
public class Identified {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

}

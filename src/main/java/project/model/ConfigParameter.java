package project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "config")
@Getter
@Setter
public class ConfigParameter extends Identified {

    private String name;
    private String value;

}

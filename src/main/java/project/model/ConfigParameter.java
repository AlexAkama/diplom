package project.model;

import javax.persistence.*;

@Entity
@Table(name = "config")
@Getter
@Setter
public class ConfigParameter extends Identified {

    private String name;

    private String value;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

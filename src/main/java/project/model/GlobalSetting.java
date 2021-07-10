package project.model;

import project.model.enums.GlobalSettingsValue;

import javax.persistence.*;

@Entity
@Table(name = "global_settings")
@Getter
@Setter
public class GlobalSetting extends Identified {

    @Column(nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(length = 3, columnDefinition = "varchar(3) default 'YES'")
    private GlobalSettingsValue value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public GlobalSettingsValue getValue() {
        return value;
    }

    public void setValue(GlobalSettingsValue value) {
        this.value = value;
    }
}

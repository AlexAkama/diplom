package project.model;

import project.model.emun.GlobalSettingsValue;

import javax.persistence.*;

@Entity
@Table(name = "global_settings")
public class GlobalSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String code;

//    @Column(nullable = false)
//    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 3, columnDefinition = "varchar(3) default 'NO'")
    private GlobalSettingsValue value;

    public GlobalSetting() {
    }

//    public GlobalSetting(String code, String name) {
//        this.code = code;
//        this.name = name;
//        this.value = GlobalSettingsValue.NO;
//    }

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

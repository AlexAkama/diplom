package project.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "captcha_codes")
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaCode extends Identified {

    @Column(nullable = false)
    private Date time;

    @Column(columnDefinition = "tinytext", nullable = false)
    private String code;

    @Column(name = "secret_code", columnDefinition = "tinytext", nullable = false)
    private String secretCode;


    public String getCode() {
        return code;
    }

}

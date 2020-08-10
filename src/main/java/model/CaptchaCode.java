package model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "captcha_codes")
public class CaptchaCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private Date time;

    @Column(columnDefinition = "tinytext", nullable = false)
    private String code;

    @Column(name = "secret_code", columnDefinition = "tinytext", nullable = false)
    private String secretCode;

    public CaptchaCode() {
    }

    public CaptchaCode(Date time, String code, String secretCode) {
        this.time = time;
        this.code = code;
        this.secretCode = secretCode;
    }
}

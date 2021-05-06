package project.model;

import org.hibernate.annotations.Type;
import project.model.emun.Role;

import javax.persistence.*;
import java.util.Date;

/**
 * Модель пользователя
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Порядковый номер в базе (уникальный, автоинкримент)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Признак модератора
     */
    @Column(name = "is_moderator", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean moderator;

    /**
     * Время регистрации
     */
    @Column(name = "reg_time", nullable = false)
    private Date registrationTime;

    /**
     * Имя пользователя
     */
    @Column(nullable = false)
    private String name;

    /**
     * Электроная почта пользователя,
     * <br>ипользуется для входа
     */
    @Column(nullable = false)
    private String email;

    /**
     * Пароль пользователя
     */
    @Column(nullable = false)
    private String password;

    /**
     * Код для восстановления пароля
     */
    private String code;

    /**
     * Ссылка на аватар пользователя
     */
    private String photo;


    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.registrationTime = new Date();
        this.moderator = false;
    }

    public Role getRole() {
        return isModerator()
                ? Role.MODERATOR
                : Role.USER;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isModerator() {
        return moderator;
    }

    public void setModerator(boolean moderator) {
        this.moderator = moderator;
    }

    public Date getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Date registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}

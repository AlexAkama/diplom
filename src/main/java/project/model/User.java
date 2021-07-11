package project.model;

import lombok.*;
import org.hibernate.annotations.Type;
import project.model.enums.Role;

import javax.persistence.*;
import java.util.Date;

/**
 * Модель пользователя
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User extends Identified {

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

}

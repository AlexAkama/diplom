package project.dto.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * Данные для изменения данных пользователя:
 * <pre>    - имя пользователя</pre>
 * <pre>    - электронная почта</pre>
 * <pre>    - пароль</pre>
 * <pre>    - файл для замены аватары</pre>
 * <pre>    - признак удаления/замены автатара</pre>
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@Getter
@Setter
public class UserUpdateDto {

    /**
     * Новое имя пользователя
     */
    private String name;

    /**
     * Новая электронная почта пользователя
     */
    private String email;

    /**
     * Новый пароль пользователя
     */
    private String password;

    /**
     * Бинанрый файл или пустое значение при удалении
     */
    private String photo;

    /**
     * Указатель на удаление файла фотографию
     * <br> 0=замена, 1=удаление
     */
    private int removePhoto;

}

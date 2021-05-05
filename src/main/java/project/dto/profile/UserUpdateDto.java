package project.dto.profile;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Данные для изменения данных пользователя:
 * <pre>    - имя пользователя</pre>
 * <pre>    - электронная почта</pre>
 * <pre>    - пароль</pre>
 * <pre>    - файл для замены аватары</pre>
 * <pre>    - признак удаления/замены автатара</pre>
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
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


    // GETTERS & SETTERS

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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getRemovePhoto() {
        return removePhoto;
    }

    public void setRemovePhoto(int removePhoto) {
        this.removePhoto = removePhoto;
    }
}

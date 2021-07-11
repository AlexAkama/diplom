package project.dto.profile;

import project.dto.image.ImageErrorMap;
import project.dto.main.AppErrorMap;

public class ProfileErrorMap extends AppErrorMap {

    private static final String PHOTO = "photo";

    public void addEmailError(String email) {
        getErrors().put("email", String.format("E-mail %s уже зарегистривован", email));
    }

    public void addPhotoSizeError(long fileSize, long maxSize) {
        getErrors().put(PHOTO, String.format("Размер файла %dMB. Максимально %dMB", fileSize, maxSize));
    }

    public void addPhotoFormatError(String format) {
        getErrors().put(PHOTO, String.format("Файл не соответствует формату (%s)", format));
    }

    public void addPhotoError(ImageErrorMap imageErrors) {
        getErrors().put(PHOTO, imageErrors.getErrors().get("image"));
    }

    public void addNameError() {
        getErrors().put("name", "Имя указано не верно");
    }

    public void addPasswordError() {
        getErrors().put("password", "Пароль короче 6-ти смиволов");
    }
}

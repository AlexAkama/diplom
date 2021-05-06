package project.dto.image;

import project.dto.main.AppErrorMap;

public class ImageErrorMap extends AppErrorMap {

    public void addSizeError(long fileSize, long maxSize) {
        getErrors().put("image", String.format("Размер файла %dMB. Максимально %dMB", fileSize, maxSize));
    }

    public void addFormatError(String format) {
        getErrors().put("image", String.format("Файл не соответствует формату (%s)", format));
    }

    public void addNotFoundError() {
        getErrors().put("image", "Файл не найден");
    }

}

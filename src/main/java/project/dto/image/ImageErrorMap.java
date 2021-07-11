package project.dto.image;

import project.dto.main.AppErrorMap;

public class ImageErrorMap extends AppErrorMap {

    private static final String IMAGE = "image";

    public void addSizeError(long fileSize, long maxSize) {
        getErrors().put(IMAGE, String.format("Размер файла %dMB. Максимально %dMB", fileSize, maxSize));
    }

    public void addFormatError(String format) {
        getErrors().put(IMAGE, String.format("Файл не соответствует формату (%s)", format));
    }

    public void addNotFoundError() {
        getErrors().put(IMAGE, "Файл не найден");
    }

}

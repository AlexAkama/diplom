package project.config;

import java.util.Date;

public class AppConstant {

    /**
     * <h3>Константы приложения</h3>
     */
    private AppConstant() {
    }

    /**
     * Перевод даты в кол-во секунд (timestamp)
     *
     * @param date дата для вычисления
     * @return кол-во секунд
     */
    public static long dateToTimestamp(Date date) {
        if (date == null) return 0;
        return date.getTime() / 1000;
    }

    /**
     * Перевод минут в милисекунды
     *
     * @param minutes кол-во минут
     * @return кол-во миллисекунд
     */
    public static long minuteToMillis(long minutes) {
        return minutes * 60 * 1000;
    }

    /**
     * Перевод байтов в мегабайты
     *
     * @param bytes кол-во байтов
     * @return кол-во мегабайтов
     */
    public static long byteToMb(long bytes) {
        return bytes / 1024 / 1024;
    }

}

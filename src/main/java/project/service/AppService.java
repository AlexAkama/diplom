package project.service;

import java.util.Date;

/**
 * <h2>Сервис вспомогательных функций</h2>
 * <tr><td>{@link AppService#dateToTimestamp(Date)}</td><td>Перевод даты в секунды</td></tr>
 * <tr><td>{@link AppService#minuteToMillis(long)}</td><td>Перевод секунд в милисекунды</td></tr>
 * <tr><td>{@link AppService#randomString(int)}</td><td>Создание случайной строки</td></tr>
 * <tr><td>...</td><td>...</td></tr>
 */
public interface AppService {

    /**
     * Перевод даты в кол-во секунд (timestamp)
     * @param date дата для вычисления
     * @return кол-во секунд
     */
    long dateToTimestamp(Date date);

    /**
     * Перевод минут в милисекунды
     * @param minutes кол-во минут
     * @return кол-во миллисекунд
     */
    long minuteToMillis(long minutes);

    /**
     * Генерация строки из случайныйх символов заданной длины
     * <br></br> Символы выбираются из указанного массива
     * @param length длина результирующей строки
     * @return строка случайных символов заданной длинны
     */
    String randomString(int length);
}

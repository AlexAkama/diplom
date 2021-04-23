package project.config;

import java.util.Date;

public class AppConstant {

    /**
     * <h3>Базовое условия для поиска постов</h3>
     * <tr><td><b>p.isActive = 1</b></td>- пост должен быть активным<td></td></tr>
     * <tr><td><b>p.moderationStatus = 'ACCEPTED'</b></td>- пост должен быть одобрен модератором<td></td></tr>
     * <tr><td><b>p.time < NOW()</b></td><td>- пост должен иметь времея публикации до текущего момента</td></tr>
     */
    public static final
    String HQL_BASIC_SEARCH_CONDITION = "p.active = true AND p.moderationStatus = 'ACCEPTED' AND p.time < function('NOW')";

    /**
     * <h3>Константы приложения</h3>
     * {@link AppConstant#HQL_BASIC_SEARCH_CONDITION Базовое условие поиска}
     */
    private AppConstant() {
    }

    /**
     * Перевод даты в кол-во секунд (timestamp)
     * @param date дата для вычисления
     * @return кол-во секунд
     */
    public static long dateToTimestamp(Date date) {
        return date.getTime() / 1000;
    }

    /**
     * Перевод минут в милисекунды
     * @param minutes кол-во минут
     * @return кол-во миллисекунд
     */
    public static long minuteToMillis(long minutes) {
        return minutes * 60 * 1000;
    }

}

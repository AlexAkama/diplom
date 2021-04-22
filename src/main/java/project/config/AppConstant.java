package project.config;

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

}

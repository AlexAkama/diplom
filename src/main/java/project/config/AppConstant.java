package project.config;

public class AppConstant {

    /**
     * Базовое условия для поиска постов
     * <br><b>p.isActive=1</b> - пост должен быть активным
     * <br><b>p.moderationStatus='ACCEPTED'</b> - пост должен быть одобрен модератором
     * <br><b>p.time<NOW()</b> - тост должен иметь времея публикации до текущего момента
     */
    private static final String HQL_BASIC_SEARCH_TERMS = "p.isActive=1 AND p.moderationStatus='ACCEPTED' AND p.time<NOW()";

    /**
     * Константы приложения
     */
    private AppConstant() {
    }

}

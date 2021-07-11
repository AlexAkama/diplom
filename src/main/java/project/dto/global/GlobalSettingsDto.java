package project.dto.global;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <h3>Глобальные настройки блога</h3>
 * <tr><td>{@link GlobalSettingsDto#multiUser}</td>Разрешение регистрации<td></td></tr>
 * <tr><td>{@link GlobalSettingsDto#preModeration}</td><td>Обязательная премодерация</td></tr>
 * <tr><td>{@link GlobalSettingsDto#publicStatistic}</td>Доступонсть статистики<td></td></tr>
 */
@AllArgsConstructor
@Getter
public class GlobalSettingsDto {

    /**
     * Признак разрешения регистрации новых пользователей: Разрешено / Запрещено
     */
    @JsonProperty("MULTIUSER_MODE")
    private final boolean multiUser;

    /**
     * Признак модерации постов перед публикацией: Обязательная / Отсутствует
     */
    @JsonProperty("POST_PREMODERATION")
    private final boolean preModeration;

    /**
     * Признак доступности статискики блога: Доступна всем / Только модераторам
     */
    @JsonProperty("STATISTICS_IS_PUBLIC")
    private final boolean publicStatistic;

}

package project.service;

import org.springframework.http.ResponseEntity;
import project.dto.global.*;

/**
 * <h2>Сервис обработки иформационных запросов блога</h2>
 * <tr><td>{@link GlobalService#getPersonalInfo()}</td><td>Получение персональных данных </td></tr>
 * <tr><td>{@link GlobalService#getGlobalSettings()}</td><td>Получение глобальных насроек блога</td></tr>
 * <tr><td>{@link GlobalService#getTagList()}</td><td>Получение списка тегов</td></tr>
 * <tr><td>{@link GlobalService#getCalendar(int)}</td><td>Получение карты кол-ва публикаций постов</td></tr>
 */
public interface GlobalService {

    /**
     * Получение персональной информации о владельце блога
     *
     * @return объект с {@link PersonalInfoDto персональными данными}
     */
    ResponseEntity<PersonalInfoDto> getPersonalInfo();

    /**
     * Получение глобальных насроек блога
     *
     * @return объект с {@link GlobalSettingsDto глобальными настройками}
     */
    ResponseEntity<GlobalSettingsDto> getGlobalSettings();

    /**
     * Сохранение глобальных настроек блока
     *
     * @param settings объект с {@link GlobalSettingsDto глобальными настройками}
     */
    void saveGlobalSettings(GlobalSettingsDto settings);

    /**
     * Получение списка тегов
     *
     * @return объект со {@link TagListDto списком тегов}
     */
    ResponseEntity<TagListDto> getTagList();

    /**
     * Получение карты кол-ва публикаций постов
     *
     * @param year год поска публикаций
     * @return объект с {@link CalendarDto данными кол-ва публикаций за год}
     */
    ResponseEntity<CalendarDto> getCalendar(int year);

}

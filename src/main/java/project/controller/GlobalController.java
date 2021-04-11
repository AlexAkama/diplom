package project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.global.*;
import project.service.GlobalService;

/**
 * <h2>Контроллер обработки информационных запросов блога</h2>
 */
@RestController
public class GlobalController {

    private final GlobalService globalService;

    public GlobalController(GlobalService globalService) {
        this.globalService = globalService;
    }

    /**
     * Запрос на получение данных об автое блога
     *
     * @return объект с {@link PersonalInfoDto персональными данными}
     */
    @GetMapping("/api/init")
    public ResponseEntity<PersonalInfoDto> getPersonalInformation() {
        return globalService.getPersonalInfo();
    }

    /**
     * Запрос глобальных настроек
     *
     * @return объект с {@link GlobalSettingsDto глобальными настройками}
     */
    @GetMapping("/api/settings")
    public ResponseEntity<GlobalSettingsDto> getSettings() {
        return globalService.getGlobalSettings();
    }

    /**
     * Запрос списка тегов
     * @param query значение для поска тега
     * @return объекта со {@link TagListDto списком тегов}
     */
    @GetMapping("/api/tag")
    public ResponseEntity<TagListDto> getTags(@RequestParam(value = "query", required = false) String query) {
        return globalService.getTagList();
    }

    /**
     * Запрос списка кол-ва публикаций в день
     * @param year год поиска публикация
     * @return объект с {@link CalendarDto данными кол-ва публикаций за год}
     */
    @GetMapping("/api/calendar")
    public ResponseEntity<CalendarDto> getCalendar(@RequestParam(value = "year", defaultValue = "2021") int year) {
        return globalService.getCalendar(year);
    }

}

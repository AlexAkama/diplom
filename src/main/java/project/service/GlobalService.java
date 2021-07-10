package project.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.dto.global.*;
import project.dto.post.PostYearDto;
import project.model.ConfigParameter;
import project.model.GlobalSetting;
import project.model.enums.GlobalSettingsValue;
import project.repository.*;

import java.util.*;
import java.util.stream.Collectors;

import static project.model.enums.GlobalSettings.*;
import static project.model.enums.GlobalSettingsValue.NO;
import static project.model.enums.GlobalSettingsValue.YES;

/**
 * <h2>Реализация сервиса обработки глобальных запросов блога</h2>
 */
@Service
public class GlobalService {

    /**
     * Минимальный вес тега, для корекного визуального отображения на фронте
     */
    private static final double MIN_WEIGHT = 0.25;

    private final ConfigParameterRepository configParameterRepository;
    private final GlobalSettingRepository globalSettingRepository;
    private final TagToPostRepository tagToPostRepository;
    private final PostRepository postRepository;

    public GlobalService(ConfigParameterRepository configParameterRepository,
                         GlobalSettingRepository globalSettingRepository,
                         TagToPostRepository tagToPostRepository,
                         PostRepository postRepository) {
        this.configParameterRepository = configParameterRepository;
        this.globalSettingRepository = globalSettingRepository;
        this.tagToPostRepository = tagToPostRepository;
        this.postRepository = postRepository;
    }

    /**
     * Получение персональной информации о владельце блога
     *
     * @return объект с {@link PersonalInfoDto персональными данными}
     */
    public ResponseEntity<PersonalInfoDto> getPersonalInfo() {

        var infoDto = new PersonalInfoDto();

        Optional<ConfigParameter> title = configParameterRepository.findConfigParameterByName("title");
        infoDto.setTitle(title.isPresent() ? title.get().getValue() : "");

        Optional<ConfigParameter> subtitle = configParameterRepository.findConfigParameterByName("subtitle");
        infoDto.setSubtitle(subtitle.isPresent() ? subtitle.get().getValue() : "");

        Optional<ConfigParameter> phone = configParameterRepository.findConfigParameterByName("phone");
        infoDto.setPhone(phone.isPresent() ? phone.get().getValue() : "");

        Optional<ConfigParameter> email = configParameterRepository.findConfigParameterByName("email");
        infoDto.setEmail(email.isPresent() ? email.get().getValue() : "");

        Optional<ConfigParameter> copyright = configParameterRepository.findConfigParameterByName("copyright");
        infoDto.setCopyright(copyright.isPresent() ? copyright.get().getValue() : "");

        Optional<ConfigParameter> copyrightFrom = configParameterRepository.findConfigParameterByName("copyrightYear");
        infoDto.setCopyrightFrom(copyrightFrom.isPresent() ? copyrightFrom.get().getValue() : "");

        return ResponseEntity.ok(infoDto);
    }

    /**
     * Получение глобальных насроек блога
     *
     * @return объект с {@link GlobalSettingsDto глобальными настройками}
     */
    public ResponseEntity<GlobalSettingsDto> getGlobalSettings() {

        Optional<GlobalSetting> optionalMultiUser = globalSettingRepository.findByCode(MULTIUSER_MODE.name());
        boolean multiUser = optionalMultiUser.isPresent() && optionalMultiUser.get().getValue() == YES;

        Optional<GlobalSetting> optionalPreModeration = globalSettingRepository.findByCode(POST_PREMODERATION.name());
        boolean preModeration = optionalPreModeration.isPresent() && optionalPreModeration.get().getValue() == YES;

        Optional<GlobalSetting> optionalPublicStatistic = globalSettingRepository.findByCode(STATISTICS_IS_PUBLIC.name());
        boolean publicStatistic = optionalPublicStatistic.isPresent() && optionalPublicStatistic.get().getValue() == YES;

        return ResponseEntity.ok(new GlobalSettingsDto(multiUser, preModeration, publicStatistic));
    }

    /**
     * Сохранение глобальных настроек блока
     *
     * @param settings объект с {@link GlobalSettingsDto глобальными настройками}
     */
    public void saveGlobalSettings(GlobalSettingsDto settings) {

        Optional<GlobalSetting> optionalMultiUser = globalSettingRepository.findByCode(MULTIUSER_MODE.name());
        if (optionalMultiUser.isPresent()) {
            GlobalSetting multiUser = optionalMultiUser.get();
            if (!Objects.equals(settings.isMultiUser(), multiUser.getValue() == YES)) {
                multiUser.setValue(getGlobalSettingsValue(settings.isMultiUser()));
                globalSettingRepository.save(multiUser);
            }
        }

        Optional<GlobalSetting> optionalPreModeration = globalSettingRepository.findByCode(POST_PREMODERATION.name());
        if (optionalPreModeration.isPresent()) {
            GlobalSetting preModeration = optionalPreModeration.get();
            if (!Objects.equals(settings.isPreModeration(), preModeration.getValue() == YES)) {
                preModeration.setValue(getGlobalSettingsValue(settings.isPreModeration()));
                globalSettingRepository.save(preModeration);
            }
        }

        Optional<GlobalSetting> optionalPublicStatistic = globalSettingRepository.findByCode(STATISTICS_IS_PUBLIC.name());
        if (optionalPublicStatistic.isPresent()) {
            GlobalSetting publicStatistic = optionalPublicStatistic.get();
            if (!Objects.equals(settings.isPublicStatistic(), publicStatistic.getValue() == YES)) {
                publicStatistic.setValue(getGlobalSettingsValue(settings.isPublicStatistic()));
                globalSettingRepository.save(publicStatistic);
            }
        }
    }

    /**
     * Получение списка тегов
     *
     * @return объект со {@link TagListDto списком тегов}
     */
    public ResponseEntity<TagListDto> getTagList() {
        List<MapDto> list = tagToPostRepository.getTagCounterList();
        Optional<MapDto> optionalTagCounter = list.stream().max(Comparator.comparingLong(MapDto::getValue));
        double maxCounter = optionalTagCounter.isPresent() ? optionalTagCounter.get().getValue() : 1;
        List<TagDto> tagListWithWeight = list.stream()
                .map(mapDto -> {
                    double weight = Math.max(mapDto.getValue() / maxCounter, MIN_WEIGHT);
                    weight = (double) (int) (weight * 100) / 100;
                    return new TagDto(mapDto.getKey(), weight);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(new TagListDto(tagListWithWeight));
    }

    /**
     * Получение карты кол-ва публикаций постов
     *
     * @param year год поска публикаций
     * @return объект с {@link CalendarDto данными кол-ва публикаций за год}
     */
    public ResponseEntity<CalendarDto> getCalendar(int year) {
        List<PostYearDto> list = postRepository.getYearList();
        List<String> yearList = list.stream().map(PostYearDto::getYear).collect(Collectors.toList());

        List<MapDto> postCounterList = postRepository.getPostCounterList(year);
        Map<String, Long> postCounterMap = postCounterList.stream()
                .collect(Collectors.toMap(MapDto::getKey, MapDto::getValue));

        return ResponseEntity.ok(new CalendarDto(yearList, postCounterMap));
    }

    private GlobalSettingsValue getGlobalSettingsValue(boolean b) {
        return b ? YES : NO;
    }

}

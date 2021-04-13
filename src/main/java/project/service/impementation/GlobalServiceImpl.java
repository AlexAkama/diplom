package project.service.impementation;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.dto._post.PostYearDto;
import project.dto.global.*;
import project.model.ConfigParameter;
import project.model.GlobalSetting;
import project.repository.*;
import project.service.GlobalService;

import java.util.*;
import java.util.stream.Collectors;

import static project.model.emun.GlobalSettings.MULTIUSER_MODE;
import static project.model.emun.GlobalSettings.POST_PREMODERATION;
import static project.model.emun.GlobalSettingsValue.YES;

/**
 * <h2>Реализация сервиса обработки глобальных запросов блога</h2>
 */
@Service
public class GlobalServiceImpl implements GlobalService {

    private static final double MIN_WEIGHT = 0.25;

    private final ConfigParameterRepository configParameterRepository;
    private final GlobalSettingRepository globalSettingRepository;
    private final _TagToPostRepository tagToPostRepository;
    private final _PostRepository postRepository;

    public GlobalServiceImpl(ConfigParameterRepository configParameterRepository,
                             GlobalSettingRepository globalSettingRepository,
                             _TagToPostRepository tagToPostRepository,
                             _PostRepository postRepository) {
        this.configParameterRepository = configParameterRepository;
        this.globalSettingRepository = globalSettingRepository;
        this.tagToPostRepository = tagToPostRepository;
        this.postRepository = postRepository;
    }

    @Override
    public ResponseEntity<PersonalInfoDto> getPersonalInfo() {

        PersonalInfoDto infoDto = new PersonalInfoDto();

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

    @Override
    public ResponseEntity<GlobalSettingsDto> getGlobalSettings() {

        Optional<GlobalSetting> optionalMultiUser = globalSettingRepository.findByCode(MULTIUSER_MODE.name());
        boolean multiUser = optionalMultiUser.isPresent() && optionalMultiUser.get().getValue() == YES;

        Optional<GlobalSetting> optionalPreModeration = globalSettingRepository.findByCode(POST_PREMODERATION.name());
        boolean preModeration = optionalPreModeration.isPresent() && optionalPreModeration.get().getValue() == YES;

        Optional<GlobalSetting> optionalPublicStatistic = globalSettingRepository.findByCode(MULTIUSER_MODE.name());
        boolean publicStatistic = optionalPublicStatistic.isPresent() && optionalPublicStatistic.get().getValue() == YES;

        return ResponseEntity.ok(new GlobalSettingsDto(multiUser, preModeration, publicStatistic));
    }

    @Override
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

    @Override
    public ResponseEntity<CalendarDto> getCalendar(int year) {
        List<PostYearDto> list = postRepository.getYearList();
        List<String> yearList = list.stream().map(PostYearDto::getYear).collect(Collectors.toList());

        List<MapDto> postCounterList = postRepository.getPostCounterList(year);
        Map<String, Long> postCounterMap = postCounterList.stream()
                .collect(Collectors.toMap(MapDto::getKey, MapDto::getValue));

        return ResponseEntity.ok(new CalendarDto(yearList, postCounterMap));
    }

}

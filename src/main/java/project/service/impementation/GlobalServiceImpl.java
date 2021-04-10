package project.service.impementation;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.dto.global.TagListDto;
import project.dto.global.*;
import project.model.ConfigParameter;
import project.model.GlobalSetting;
import project.repository.ConfigParameterRepository;
import project.repository.GlobalSettingRepository;
import project.service.GlobalService;

import java.util.Optional;

import static project.model.emun.GlobalSettings.*;
import static project.model.emun.GlobalSettingsValue.*;

@Service
public class GlobalServiceImpl implements GlobalService {

    private final ConfigParameterRepository configParameterRepository;
    private final GlobalSettingRepository globalSettingRepository;

    public GlobalServiceImpl(ConfigParameterRepository configParameterRepository,
                             GlobalSettingRepository globalSettingRepository) {
        this.configParameterRepository = configParameterRepository;
        this.globalSettingRepository = globalSettingRepository;
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
        return null;
    }

    @Override
    public ResponseEntity<CalendarDto> getCalendar() {
        return null;
    }

}

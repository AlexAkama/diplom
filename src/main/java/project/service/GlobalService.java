package project.service;

import project.dto.global.CalendarDto;
import project.dto.global.GlobalSettingsDto;
import project.dto.global.TagListDto;
import project.dto.global.PersonalInfoDto;
import org.springframework.http.ResponseEntity;

public interface GlobalService {

    ResponseEntity<PersonalInfoDto> getPersonalInfo();

    ResponseEntity<GlobalSettingsDto> getGlobalSettings();

    ResponseEntity<TagListDto> getTagList();

    ResponseEntity<CalendarDto> getCalendar();
}

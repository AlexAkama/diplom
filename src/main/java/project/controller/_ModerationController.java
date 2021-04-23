package project.controller;

import org.springframework.web.bind.annotation.*;
import project.dto.global.GlobalSettingsDto;

import static project.dto.Dto.saveGlobalParameter;
import static project.model.emun.GlobalSettings.*;

@RestController
public class _ModerationController {

    @PostMapping("/api/settings")
    public void setGlobalSettings(@RequestBody GlobalSettingsDto oldSettings) {

        saveGlobalParameter(MULTIUSER_MODE.name(), oldSettings.isMultiUser());
        saveGlobalParameter(POST_PREMODERATION.name(), oldSettings.isPreModeration());
        saveGlobalParameter(STATISTICS_IS_PUBLIC.name(), oldSettings.isPublicStatistic());

    }
}

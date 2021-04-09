package project.dto.global;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GlobalSettingsDto {
    @JsonProperty("MULTIUSER_MODE")
    private final boolean multiUser;
    @JsonProperty("POST_PREMODERATION")
    private final boolean preModeration;
    @JsonProperty("STATISTICS_IS_PUBLIC")
    private final boolean publicStatistic;

    public GlobalSettingsDto(boolean multiUser, boolean preModeration, boolean publicStatistic) {
        this.multiUser = multiUser;
        this.preModeration = preModeration;
        this.publicStatistic = publicStatistic;
    }

    public boolean isMultiUser() {
        return multiUser;
    }

    public boolean isPreModeration() {
        return preModeration;
    }

    public boolean isPublicStatistic() {
        return publicStatistic;
    }

}

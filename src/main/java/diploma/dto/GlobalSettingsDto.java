package diploma.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GlobalSettingsDto {
    @JsonProperty("MULTIUSER_MODE")
    private boolean multiUser;
    @JsonProperty("POST_PREMODERATION")
    private boolean preModeration;
    @JsonProperty("STATISTICS_IS_PUBLIC")
    private boolean publicStatistic;

    public GlobalSettingsDto(boolean multiUser, boolean preModeration, boolean publicStatistic) {
        this.multiUser = multiUser;
        this.preModeration = preModeration;
        this.publicStatistic = publicStatistic;
    }

    public boolean isMultiUser() {
        return multiUser;
    }

    public void setMultiUser(boolean multiUser) {
        this.multiUser = multiUser;
    }

    public boolean isPreModeration() {
        return preModeration;
    }

    public void setPreModeration(boolean preModeration) {
        this.preModeration = preModeration;
    }

    public boolean isPublicStatistic() {
        return publicStatistic;
    }

    public void setPublicStatistic(boolean publicStatistic) {
        this.publicStatistic = publicStatistic;
    }
}

package diploma.model.emun;

public enum GlobalSettingsValue {
    NO,
    YES;

    public static GlobalSettingsValue getSetValue(boolean b) {
        return (b) ? GlobalSettingsValue.YES : GlobalSettingsValue.NO;
    }
}
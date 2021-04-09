package project.model.emun;

public enum GlobalSettings {
    MULTIUSER_MODE("Многопользовательский режим "),
    POST_PREMODERATION("Премодерация постов"),
    STATISTICS_IS_PUBLIC("Показывать всем статистику блога");

    private final String description;

    GlobalSettings(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

}

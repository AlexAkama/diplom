package project.model.enums;

public enum Permission {
    READ("user:read"),
    WRITE("user:write"),
    MODERATE("user:moderate"),
    ADMINISTRATE("user:full");

    private final String text;

    Permission(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

package diploma.model.emun;

public enum Permission {
    READER("user:read"),
    WRITER("user:write"),
    MODERATOR("user:moderate"),
    ADMINISTRATOR("user:full");

    private final String permission;

    // CONSTRUCTORS
    Permission(String permission) {
        this.permission = permission;
    }

    // GETTERS & SETTERS
    public String getPermission() {
        return permission;
    }
}

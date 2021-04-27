package project.model.emun;

public enum Permission {
    READ("user:read"),
    WRITE("user:write"),
    MODERATE("user:moderate"),
    ADMINISTRATE("user:full");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}

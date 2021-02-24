package diploma.model.emun;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    GUEST(Set.of(Permission.READER)),
    USER(Set.of(Permission.READER, Permission.WRITER)),
    MODERATOR(Set.of(Permission.READER, Permission.WRITER, Permission.MODERATOR)),
    ADMINISTRATOR(Set.of(Permission.READER, Permission.WRITER, Permission.MODERATOR, Permission.ADMINISTRATOR));

    private final Set<Permission> permissions;

    // CONSTRUCTORS
    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    //METHODS
    public Set<SimpleGrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }

    // GETTERS & SETTERS
    public Set<Permission> getPermissions() {
        return permissions;
    }
}

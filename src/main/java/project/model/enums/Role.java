package project.model.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static project.model.enums.Permission.*;

public enum Role {
    GUEST(Set.of(READ)),
    USER(Set.of(READ, WRITE)),
    MODERATOR(Set.of(READ, WRITE, MODERATE)),
    ADMINISTRATOR(Set.of(READ, WRITE, MODERATE, ADMINISTRATE));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getText()))
                .collect(Collectors.toSet());
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}

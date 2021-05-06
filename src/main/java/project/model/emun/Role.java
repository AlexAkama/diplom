package project.model.emun;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static project.model.emun.Permission.*;

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
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}

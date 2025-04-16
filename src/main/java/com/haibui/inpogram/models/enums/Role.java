package com.haibui.inpogram.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.haibui.inpogram.models.enums.Permission.ADMIN_CREATE;
import static com.haibui.inpogram.models.enums.Permission.ADMIN_DELETE;
import static com.haibui.inpogram.models.enums.Permission.ADMIN_READ;
import static com.haibui.inpogram.models.enums.Permission.ADMIN_UPDATE;
import static com.haibui.inpogram.models.enums.Permission.MEMBER_CREATE;
import static com.haibui.inpogram.models.enums.Permission.MEMBER_DELETE;
import static com.haibui.inpogram.models.enums.Permission.MEMBER_READ;
import static com.haibui.inpogram.models.enums.Permission.MEMBER_UPDATE;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER(Collections.emptySet()),
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_CREATE,
                    ADMIN_DELETE,
                    MEMBER_READ,
                    MEMBER_UPDATE,
                    MEMBER_CREATE,
                    MEMBER_DELETE

            )
    ),
    MEMBER(
            Set.of(
                    MEMBER_READ,
                    MEMBER_UPDATE,
                    MEMBER_CREATE,
                    MEMBER_DELETE

            )
    );

    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}

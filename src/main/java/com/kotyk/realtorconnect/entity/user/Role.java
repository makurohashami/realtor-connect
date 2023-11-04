package com.kotyk.realtorconnect.entity.user;

import lombok.Getter;

import java.util.*;
import java.util.stream.Stream;

import static com.kotyk.realtorconnect.entity.user.Permission.*;

@Getter
public enum Role {

    USER(0, List.of(ROLE_USER)),
    REALTOR(1, List.of(ROLE_USER, ROLE_REALTOR)),
    ADMIN(2, List.of(ROLE_USER, ROLE_REALTOR, ROLE_ADMIN));

    private final int roleId;
    private final List<Permission> permissions;
    private static final Map<Integer, Role> ROLE_BY_ID_MAP = Collections.unmodifiableMap(initialiseValueMapping());

    Role(int roleId, List<Permission> permissions) {
        this.roleId = roleId;
        this.permissions = permissions;
    }

    public static Role getById(int roleId) {
        return Optional.ofNullable(ROLE_BY_ID_MAP.get(roleId))
                .orElseThrow(() -> new IllegalArgumentException("Can't find Role with id: " + roleId));
    }

    private static Map<Integer, Role> initialiseValueMapping() {
        return Stream.of(values())
                .collect(HashMap::new, (map, role) -> map.put(role.roleId, role), HashMap::putAll);
    }

}

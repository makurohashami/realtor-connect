package com.kotyk.realtorconnect.entity.user;

import lombok.Getter;

import java.util.*;
import java.util.stream.Stream;

import static com.kotyk.realtorconnect.entity.user.Permission.*;

@Getter
public enum Role {

    CHIEF_ADMIN(0, List.of(MANAGE_ADMINS, MANAGE_USERS, MANAGE_REALTOR_INFO, SEE_PRIVATE_PHOTOS, ACCESS_TO_ADMIN_PANEL, SEE_PRIVATE_REAL_ESTATES)),
    ADMIN(10, List.of(MANAGE_USERS, MANAGE_REALTOR_INFO, SEE_PRIVATE_PHOTOS, ACCESS_TO_ADMIN_PANEL, SEE_PRIVATE_REAL_ESTATES)),
    REALTOR(20, List.of(SEE_PRIVATE_PHOTOS)),
    USER(30, List.of());

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

package com.kotyk.realtorconnect.entity.realestate.enumeration;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum BuildingType {

    APARTMENT(0),
    ROOM(1),
    ROOM_IN_A_COMMUNAL_APARTMENT(2),
    DEDICATED_COMMUNE(3),
    DEDICATED_COMMUNAL_BLOCK(4),
    STUDIO_FLAT(5),
    HOUSE(6);

    private final int typeId;
    private static final Map<Integer, BuildingType> TYPE_BY_ID_MAP = Collections.unmodifiableMap(initialiseValueMapping());

    BuildingType(int typeId) {
        this.typeId = typeId;
    }

    public static BuildingType getById(int typeId) {
        return Optional.ofNullable(TYPE_BY_ID_MAP.get(typeId))
                .orElseThrow(() -> new IllegalArgumentException("Can't find BuildingType with id: " + typeId));
    }

    private static Map<Integer, BuildingType> initialiseValueMapping() {
        return Stream.of(values())
                .collect(HashMap::new, (map, type) -> map.put(type.typeId, type), HashMap::putAll);
    }

}

package com.kotyk.realtorconnect.entity.realestate.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum BathroomType {

    TOILET_BATH(0),
    TOILET_SHOWER(1),
    TOILET_SHOWER_LADDER(2);

    private final int typeId;
    private static final Map<Integer, BathroomType> TYPE_BY_ID_MAP = Collections.unmodifiableMap(initialiseValueMapping());

    BathroomType(int typeId) {
        this.typeId = typeId;
    }

    public static BathroomType getById(int typeId) {
        return Optional.ofNullable(TYPE_BY_ID_MAP.get(typeId))
                .orElseThrow(() -> new IllegalArgumentException("Can't find BathroomType with id: " + typeId));
    }

    private static Map<Integer, BathroomType> initialiseValueMapping() {
        return Stream.of(values())
                .collect(HashMap::new, (map, type) -> map.put(type.typeId, type), HashMap::putAll);
    }

}

package com.kotyk.realtorconnect.entity.realestate.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum HeatingType {

    NO_HEATING(0),
    OWN_GAS(1),
    ELECTRICITY(2),
    ROOF_BOILER(3),
    CENTRALIZED(3);

    private final int typeId;
    private static final Map<Integer, HeatingType> TYPE_BY_ID_MAP = Collections.unmodifiableMap(initialiseValueMapping());

    HeatingType(int typeId) {
        this.typeId = typeId;
    }

    public static HeatingType getById(int typeId) {
        return Optional.ofNullable(TYPE_BY_ID_MAP.get(typeId))
                .orElseThrow(() -> new IllegalArgumentException("Can't find HeatingType with id: " + typeId));
    }

    private static Map<Integer, HeatingType> initialiseValueMapping() {
        return Stream.of(values())
                .collect(HashMap::new, (map, type) -> map.put(type.typeId, type), HashMap::putAll);
    }

}

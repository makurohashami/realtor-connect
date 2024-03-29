package com.kotyk.realtorconnect.entity.realestate.enumeration;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum HotWaterType {

    CENTRALIZED(0),
    ROOF_BOILER(1),
    ELECTRIC_BOILER(2),
    GAS_COLUMN(3),
    DOUBLE_CIRCUIT_GAS_BOILER(4),
    NO_HOT_WATER(5);

    private final int typeId;
    private static final Map<Integer, HotWaterType> TYPE_BY_ID_MAP = Collections.unmodifiableMap(initialiseValueMapping());

    HotWaterType(int typeId) {
        this.typeId = typeId;
    }

    public static HotWaterType getById(int typeId) {
        return Optional.ofNullable(TYPE_BY_ID_MAP.get(typeId))
                .orElseThrow(() -> new IllegalArgumentException("Can't find HotWaterType with id: " + typeId));
    }

    private static Map<Integer, HotWaterType> initialiseValueMapping() {
        return Stream.of(values())
                .collect(HashMap::new, (map, type) -> map.put(type.typeId, type), HashMap::putAll);
    }
}

package com.kotyk.realtorconnect.entity.realestate.enumeration;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum StateType {

    WITHOUT_REPAIR(0),
    EUROPEAN_STYLE_RENOVATION(1),
    LIVING(2),
    NOT_FINISHED(3),
    CAPITAL(4),
    FROM_DEVELOPER(5);

    private final int typeId;
    private static final Map<Integer, StateType> TYPE_BY_ID_MAP = Collections.unmodifiableMap(initialiseValueMapping());

    StateType(int typeId) {
        this.typeId = typeId;
    }

    public static StateType getById(int typeId) {
        return Optional.ofNullable(TYPE_BY_ID_MAP.get(typeId))
                .orElseThrow(() -> new IllegalArgumentException("Can't find StateType with id: " + typeId));
    }

    private static Map<Integer, StateType> initialiseValueMapping() {
        return Stream.of(values())
                .collect(HashMap::new, (map, type) -> map.put(type.typeId, type), HashMap::putAll);
    }
}

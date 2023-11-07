package com.kotyk.realtorconnect.entity.realestate.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum LoggiaType {

    BALCONY(0),
    LOGGIA(1);

    private final int typeId;
    private static final Map<Integer, LoggiaType> TYPE_BY_ID_MAP = Collections.unmodifiableMap(initialiseValueMapping());

    LoggiaType(int typeId) {
        this.typeId = typeId;
    }

    public static LoggiaType getById(int typeId) {
        return Optional.ofNullable(TYPE_BY_ID_MAP.get(typeId))
                .orElseThrow(() -> new IllegalArgumentException("Can't find LoggiaType with id: " + typeId));
    }

    private static Map<Integer, LoggiaType> initialiseValueMapping() {
        return Stream.of(values())
                .collect(HashMap::new, (map, type) -> map.put(type.typeId, type), HashMap::putAll);
    }

}

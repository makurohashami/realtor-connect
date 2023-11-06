package com.kotyk.realtorconnect.entity.realtor;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum ContactType {

    ADDITIONAL_PHONE(0),
    TELEGRAM(1),
    VIBER(2),
    WHATSAPP(3);

    private final int typeId;
    private static final Map<Integer, ContactType> TYPE_BY_ID_MAP = Collections.unmodifiableMap(initialiseValueMapping());

    ContactType(int typeId) {
        this.typeId = typeId;
    }

    public static ContactType getById(int typeId) {
        return Optional.ofNullable(TYPE_BY_ID_MAP.get(typeId))
                .orElseThrow(() -> new IllegalArgumentException("Can't find ContactType with id: " + typeId));
    }

    private static Map<Integer, ContactType> initialiseValueMapping() {
        return Stream.of(values())
                .collect(HashMap::new, (map, type) -> map.put(type.typeId, type), HashMap::putAll);
    }

}

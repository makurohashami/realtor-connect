package com.kotyk.realtorconnect.entity.realtor;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum SubscriptionType {

    FREE(0),
    PRO(1);

    private final int subscriptionId;
    private static final Map<Integer, SubscriptionType> SUBSCRIPTION_BY_ID_MAP = Collections.unmodifiableMap(initialiseValueMapping());

    SubscriptionType(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public static SubscriptionType getById(int subscriptionId) {
        return Optional.ofNullable(SUBSCRIPTION_BY_ID_MAP.get(subscriptionId))
                .orElseThrow(() -> new IllegalArgumentException("Can't find SubscriptionType with id: " + subscriptionId));
    }

    private static Map<Integer, SubscriptionType> initialiseValueMapping() {
        return Stream.of(values())
                .collect(HashMap::new, (map, type) -> map.put(type.subscriptionId, type), HashMap::putAll);
    }
}

package com.kotyk.realtorconnect.dto;

import java.time.Instant;

public record Error(Instant timestamp, String error, Object details, String path) {
}

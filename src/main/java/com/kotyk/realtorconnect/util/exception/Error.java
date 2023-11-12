package com.kotyk.realtorconnect.util.exception;

import java.time.Instant;

public record Error(Instant timestamp, String error, Object details, String path) {
}

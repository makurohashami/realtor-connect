package com.kotyk.realtorconnect.util;

import com.kotyk.realtorconnect.dto.apiresponse.ApiError;
import com.kotyk.realtorconnect.dto.apiresponse.ApiHttpStatus;
import com.kotyk.realtorconnect.dto.apiresponse.ApiSuccess;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseUtil<T> {

    public static <T> ResponseEntity<ApiSuccess<T>> wrapSuccess(T body, HttpStatus status) {
        ApiSuccess<T> response = new ApiSuccess<>(body, new ApiHttpStatus(status));
        return ResponseEntity.status(status).body(response);
    }

    public static <T> ResponseEntity<ApiError<T>> wrapError(T body, HttpStatus status) {
        ApiError<T> response = new ApiError<>(body, new ApiHttpStatus(status));
        return ResponseEntity.status(status).body(response);
    }

    public static <T> ResponseEntity<ApiSuccess<T>> ok(T body) {
        return wrapSuccess(body, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ApiSuccess<T>> created(T body) {
        return wrapSuccess(body, HttpStatus.CREATED);
    }

    public static <T> ResponseEntity<ApiError<T>> badRequest(T body) {
        return wrapError(body, HttpStatus.BAD_REQUEST);
    }

    public static <T> ResponseEntity<ApiError<T>> notFound(T body) {
        return wrapError(body, HttpStatus.NOT_FOUND);
    }

}

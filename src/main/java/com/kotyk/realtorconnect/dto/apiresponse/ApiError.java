package com.kotyk.realtorconnect.dto.apiresponse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApiError<T> {

    private ApiHttpStatus status;
    private T error;
    private boolean success = false;

    public ApiError(T error, ApiHttpStatus status) {
        this.error = error;
        this.status = status;
    }

}

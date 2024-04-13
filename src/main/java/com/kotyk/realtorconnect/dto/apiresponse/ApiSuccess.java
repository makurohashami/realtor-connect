package com.kotyk.realtorconnect.dto.apiresponse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApiSuccess<T> {

    private ApiHttpStatus status;
    private T result;
    private boolean success = true;

    public ApiSuccess(T result, ApiHttpStatus status) {
        this.result = result;
        this.status = status;
    }
}

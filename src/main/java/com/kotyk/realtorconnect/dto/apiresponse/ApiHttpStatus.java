package com.kotyk.realtorconnect.dto.apiresponse;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiHttpStatus {

    private String status;
    private int code;

    public ApiHttpStatus(HttpStatus httpStatus) {
        this.status = httpStatus.name();
        this.code = httpStatus.value();
    }

}

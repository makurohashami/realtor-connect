package com.kotyk.realtorconnect.dto.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Email {

    private String to;
    private String subject;
    private boolean isHtml;
    private String body;

}

package com.kotyk.realtorconnect.dto.email;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Email {

    private String to;
    private String subject;
    private boolean isHtml;
    @ToString.Exclude
    private String body;

}

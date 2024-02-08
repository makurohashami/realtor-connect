package com.kotyk.realtorconnect.service.email;

import com.kotyk.realtorconnect.dto.email.Email;

public interface EmailSenderService {

    void sendEmail(Email email);

}

package com.kotyk.realtorconnect.dto.realtor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealtorFilter {

    private String name;
    private String agency;
    private String phone;

}

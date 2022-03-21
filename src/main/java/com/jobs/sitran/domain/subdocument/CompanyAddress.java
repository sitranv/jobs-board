package com.jobs.sitran.domain.subdocument;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyAddress {

    private String houseNumber;

    private String streetAddress;

    private String city;

    private String state;

    private String zipCode;

    private String country;

    private Double latitude;

    private Double longitude;
}

package com.jobs.sitran.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company extends AbstractAuditEntity implements Serializable {

    @Id
    private String id;

    private String name;

    private String companyLogo;

    private String description;

    private String companyAddress;

    private String employerId;

}

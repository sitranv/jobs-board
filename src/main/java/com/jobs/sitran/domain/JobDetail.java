package com.jobs.sitran.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Document("jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobDetail extends AbstractAuditEntity implements Serializable {

    @Id
    private String id;

    private String title;

    private String companyId;

    private Long jobPostdateInMillis;

    private String description;

    private Instant deadline;

    private String salaryRange;

    private String benefit;

    private String request;

    private List<String> hashtags;

    private List<String> place;

    private Company companyMetaData;

    private Boolean isOpening;
}

package com.jobs.sitran.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document("job_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobApplication extends AbstractAuditEntity implements Serializable {

    @Id
    private String id;

    private String userId;

    private String jobId;

    private String userCv;

    private Double match;
}

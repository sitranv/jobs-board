package com.jobs.sitran.model.dto;

import com.jobs.sitran.domain.JobDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JobApplicationDTO {

    private String id;

    private JobDetail job;

    private String userCV;

    private Instant appliedDate;

}

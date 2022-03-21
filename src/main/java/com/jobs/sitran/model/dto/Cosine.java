package com.jobs.sitran.model.dto;

import com.jobs.sitran.domain.JobDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cosine {

    private JobDetail job;

    private Double mark;
}

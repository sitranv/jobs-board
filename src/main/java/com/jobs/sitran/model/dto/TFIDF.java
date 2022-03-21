package com.jobs.sitran.model.dto;

import com.jobs.sitran.domain.JobDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TFIDF {

    private JobDetail job;

    private Map<String, Double> tf_idf;
}

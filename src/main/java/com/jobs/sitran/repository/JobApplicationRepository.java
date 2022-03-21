package com.jobs.sitran.repository;

import com.jobs.sitran.domain.JobApplication;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends MongoRepository<JobApplication, String> {

    List<JobApplication> getJobApplicationsByUserIdOrderByCreatedDateDesc(String userId);

    JobApplication getJobApplicationsByUserIdAndJobId(String userId, String jobId);

    Integer countAllByJobId(String jobId);
}

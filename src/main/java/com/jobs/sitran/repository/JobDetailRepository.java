package com.jobs.sitran.repository;

import com.jobs.sitran.domain.JobDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface JobDetailRepository extends MongoRepository<JobDetail, String> {

    Page<JobDetail> findByIsOpeningTrue(Pageable pageable);

    Optional<JobDetail> findJobDetailById(String id);

    @Query("{hashtags: {$in: ?0}}")
    Page<JobDetail> getRelatedJobs(Set<String> hashtags, Pageable pageable);

    @Query(" { request: {$regex: ?0, $options: 'i'}}")
    List<JobDetail> getJobsRelatedCV(String hashtag);

    @Query("{$and: [{$or: [" +
            "{ request: {$regex: ?0, $options: 'i'}}, " +
            "{ description: {$regex: ?0, $options: 'i'}}, " +
            "{ title: {$regex: ?0, $options: 'i'}}, " +
            "{ 'companyMetaData.name': {$regex: ?0, $options: 'i'}} ]}, { isOpening: true }]}")
    Page<JobDetail> findJobs(String search, Pageable pageable);

    List<JobDetail> getJobDetailsByCompanyId(String companyId);

    Page<JobDetail> getJobDetailsByCompanyId(String companyId, Pageable pageable);

    JobDetail getJobDetailById(String id);

    Integer countAllByCompanyId(String companyId);
}

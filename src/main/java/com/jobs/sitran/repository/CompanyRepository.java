package com.jobs.sitran.repository;

import com.jobs.sitran.domain.Company;
import com.jobs.sitran.domain.JobDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends MongoRepository<Company, String> {

    List<Company> findAll();

    Optional<Company> findCompanyById(String id);

    Optional<Company> findCompanyByEmployerId(String employerId);

    Company getCompanyByEmployerId(String employerId);

    Company getCompanyById(String companyId);

    @Query("{name: {$regex: ?0, $options: 'i'}}")
    Page<Company> findCompanies(String search, Pageable pageable);
}

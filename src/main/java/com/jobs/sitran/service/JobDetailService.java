package com.jobs.sitran.service;

import com.jobs.sitran.domain.Authority;
import com.jobs.sitran.domain.Company;
import com.jobs.sitran.domain.JobApplication;
import com.jobs.sitran.domain.JobDetail;
import com.jobs.sitran.domain.subdocument.Candidate;
import com.jobs.sitran.exception.NotFoundException;
import com.jobs.sitran.model.dto.UserDTO;
import com.jobs.sitran.model.request.JobDetailRequest;
import com.jobs.sitran.repository.CompanyRepository;
import com.jobs.sitran.repository.JobApplicationRepository;
import com.jobs.sitran.repository.JobDetailRepository;
import com.jobs.sitran.repository.UserRepository;
import com.jobs.sitran.security.AuthorityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JobDetailService {

    @Autowired
    private JobDetailRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    public Page<JobDetail> getJobs(Integer numOfJobs, String search) {
        return this.jobRepository.findJobs(search == null ? "" : search.trim(), PageRequest.of(0, numOfJobs));
    }

    public JobDetail createJob(JobDetailRequest jobDetailRequest) throws NotFoundException {
        JobDetail jobDetail = new JobDetail();
        UserDTO user = this.userService.getCurrentUser();
        Company company = this.companyRepository.findCompanyByEmployerId(user.getId()).orElseThrow(
                () -> new NotFoundException("Company of Employer id: " + user.getId() + " not found!")
        );
        jobDetail.setTitle(jobDetailRequest.getTitle());
        jobDetail.setBenefit(jobDetailRequest.getBenefit());
        jobDetail.setDeadline(jobDetailRequest.getDeadline());
        jobDetail.setDescription(jobDetailRequest.getDescription());
        jobDetail.setCompanyId(company.getId());
        jobDetail.setCompanyMetaData(company);
        jobDetail.setSalaryRange(jobDetailRequest.getSalaryRange());
        jobDetail.setRequest(jobDetailRequest.getRequest());
        jobDetail.setHashtags(jobDetailRequest.getHashtags());
        jobDetail.setPlace(jobDetailRequest.getPlace());
        jobDetail.setJobPostdateInMillis(System.currentTimeMillis());
        jobDetail.setIsOpening(true);
        return this.jobRepository.save(jobDetail);
    }

    public JobDetail getJobById(String jobId) throws NotFoundException {
        JobDetail jobDetail = this.jobRepository.findJobDetailById(jobId)
                .orElseThrow(() -> new NotFoundException("Job id: " + jobId + " not found!"));
        return jobDetail;
    }

    public JobApplication userIsApplied(String jobId) {
        UserDTO user = this.userService.getCurrentUser();
        JobApplication job = this.jobApplicationRepository.getJobApplicationsByUserIdAndJobId(user.getId(), jobId);
        return job;
    }

    public List<Candidate> getCandidatesByJobId(String id) throws NotFoundException {
        UserDTO user = this.userService.getCurrentUser();

        if (!user.getAuthorities().contains(new Authority(AuthorityConstants.ADMIN))) {
            Company company = this.companyRepository.findCompanyByEmployerId(user.getId()).orElseThrow(
                    () -> new NotFoundException("Company of employer id: " + user.getId() + " not found!")
            );
        }
        JobDetail jobDetail = this.jobRepository.findJobDetailById(id)
                .orElseThrow(() -> new NotFoundException("Job id: " + id + " not found!"));
        List<Candidate> candidates = new ArrayList<>();
        if (jobDetail != null) {
            Query query = new Query();
            query.addCriteria(Criteria.where("jobId").is(id));
            query.with(Sort.by(Sort.Direction.DESC, "match")).limit(100);
            List<JobApplication> appliedUsers = mongoTemplate.find(query, JobApplication.class);
            for (JobApplication appliedUser : appliedUsers) {
                Candidate candidate = new Candidate();
                candidate.setDetail(appliedUser);
                candidate.setUser(this.userService.getUserDTOById(appliedUser.getUserId()));
                candidates.add(candidate);
            }
        }
        return candidates;
    }

    public List<JobDetail> getRecentJob() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "createdDate")).limit(100);
        return mongoTemplate.find(query, JobDetail.class);
    }

    public List<JobDetail> getRelatedJobs(Set<String> hashtags, String jobId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("hashtags").in(hashtags));
        query.addCriteria(Criteria.where("id").ne(jobId));
        query.with(Sort.by(Sort.Direction.DESC, "createdDate")).limit(5);
        return mongoTemplate.find(query, JobDetail.class);
    }

    public Page<JobDetail> getAllJobsByEmployer() throws NotFoundException {
        UserDTO user = this.userService.getCurrentUser();
        if (user.getAuthorities().contains(new Authority(AuthorityConstants.ADMIN))) {
            return this.jobRepository.findAll(PageRequest.of(0, 300));
        } else {
            Company company = this.companyRepository.findCompanyByEmployerId(user.getId()).orElseThrow(
                    () -> new NotFoundException("Company of employer id: " + user.getId() + " not found!")
            );
            return this.jobRepository.getJobDetailsByCompanyId(company.getId(), PageRequest.of(0, 300));
        }
    }

    public Boolean deleteJobById(String id) throws NotFoundException {
        UserDTO user = this.userService.getCurrentUser();
        JobDetail jobDetail = this.jobRepository.findJobDetailById(id)
                .orElseThrow(() -> new NotFoundException("Job id: " + id + " not found!"));
        if (user.getAuthorities().contains(new Authority(AuthorityConstants.EMPLOYER))) {
            Company company = this.companyRepository.findCompanyByEmployerId(user.getId()).orElseThrow(
                    () -> new NotFoundException("Company of employer id: " + user.getId() + " not found!")
            );
            if (jobDetail != null && jobDetail.getCompanyId().equals(company.getId())) {
                this.jobRepository.delete(jobDetail);
                return true;
            }
        } else if (user.getAuthorities().contains(new Authority(AuthorityConstants.ADMIN))) {
            this.jobRepository.delete(jobDetail);
            return true;
        }

        return false;
    }

    public List<JobDetail> findAll() {
        return this.jobRepository.findAll();
    }

    public JobDetail updateJob(String jobId, JobDetailRequest jobRequest) throws NotFoundException {
        UserDTO user = this.userService.getCurrentUser();
        JobDetail jobDetail = null;
        Company company = null;
        if (user.getAuthorities().contains(new Authority(AuthorityConstants.ADMIN))) {
            jobDetail = this.jobRepository.findJobDetailById(jobId)
                    .orElseThrow(() -> new NotFoundException("Job id: " + jobId + " not found!"));
        } else {
            company = this.companyRepository.findCompanyByEmployerId(user.getId()).orElseThrow(
                    () -> new NotFoundException("Company of employer id: " + user.getId() + " not found!")
            );

            jobDetail = this.jobRepository.findJobDetailById(jobId)
                    .orElseThrow(() -> new NotFoundException("Job id: " + jobId + " not found!"));
        }

        if (user.getAuthorities().contains(new Authority(AuthorityConstants.ADMIN)) ||
        (jobDetail != null && jobDetail.getCompanyId().equals(company.getId()))) {
            if (jobRequest.getTitle() != null) {
                jobDetail.setTitle(jobRequest.getTitle());
            }

            if (jobRequest.getDescription() != null) {
                jobDetail.setDescription(jobRequest.getDescription());
            }

            if (jobRequest.getDeadline() != null) {
                jobDetail.setDeadline(jobRequest.getDeadline());
            }

            if (jobRequest.getHashtags() != null) {
                jobDetail.setHashtags(jobRequest.getHashtags());
            }

            if (jobRequest.getPlace() != null) {
                jobDetail.setPlace(jobRequest.getPlace());
            }

            if (jobRequest.getBenefit() != null) {
                jobDetail.setBenefit(jobRequest.getBenefit());
            }

            if (jobRequest.getSalaryRange() != null) {
                jobDetail.setSalaryRange(jobRequest.getSalaryRange());
            }

            if (jobRequest.getRequest() != null) {
                jobDetail.setRequest(jobRequest.getRequest());
            }
        }
        return this.jobRepository.save(jobDetail);
    }

    public Map<String, Object> dashboard() throws NotFoundException {
        Map<String, Object> response = new HashMap<>();
        UserDTO user = this.userService.getCurrentUser();
        Company company = this.companyRepository.findCompanyByEmployerId(user.getId()).orElseThrow(
                () -> new NotFoundException("Company of employer id: " + user.getId() + " not found!")
        );
        List<JobDetail> jobs = this.jobRepository.getJobDetailsByCompanyId(company.getId());
        Integer numOfCandidates = 0;
        for(JobDetail job: jobs) {
            numOfCandidates += this.jobApplicationRepository.countAllByJobId(job.getId());
        }
        response.put("numOfJobs", jobs.size());
        response.put("numOfCandidates", numOfCandidates);
        return response;
    }

    public Map<String, Object> getAdminDashboard() {
        Map<String, Object> response = new HashMap<>();
        Long numOfJobs = this.jobRepository.count();
        Long numOfCompanies = this.companyRepository.count();
        Long numOfUsers = this.userRepository.count();

        response.put("numOfJobs", numOfJobs);
        response.put("numOfCompanies", numOfCompanies);
        response.put("numOfUsers", numOfUsers);
        return response;
    }

    public Boolean toggle(String jobId) throws NotFoundException {
        UserDTO user = this.userService.getCurrentUser();
        if (user.getAuthorities().contains(new Authority(AuthorityConstants.ADMIN))) {
            JobDetail jobDetail = this.jobRepository.getJobDetailById(jobId);
            jobDetail.setIsOpening(!jobDetail.getIsOpening());
            this.jobRepository.save(jobDetail);
        } else {
            Company company = this.companyRepository.findCompanyByEmployerId(user.getId()).orElseThrow(
                    () -> new NotFoundException("Company of employer id: " + user.getId() + " not found!")
            );
            JobDetail jobDetail = this.jobRepository.getJobDetailById(jobId);
            if (jobDetail.getCompanyId().equals(company.getId())) {
                jobDetail.setIsOpening(!jobDetail.getIsOpening());
            }
            this.jobRepository.save(jobDetail);
        }
        return true;
    }
}

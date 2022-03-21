package com.jobs.sitran.service;

import com.jobs.sitran.domain.JobApplication;
import com.jobs.sitran.domain.JobDetail;
import com.jobs.sitran.exception.NotFoundException;
import com.jobs.sitran.model.dto.Cosine;
import com.jobs.sitran.model.dto.UserDTO;
import com.jobs.sitran.repository.JobApplicationRepository;
import com.jobs.sitran.repository.JobDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private JobDetailRepository jobRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RSService rsService;

    public JobApplication applyToJob(String jobId, MultipartFile userCv) throws NotFoundException, IOException {
        JobDetail jobDetail = this.jobRepository.findJobDetailById(jobId)
                .orElseThrow(() -> new NotFoundException("Job id: " + jobId + " not found!"));

        UserDTO user = this.userService.getCurrentUser();
        JobApplication find = this.jobApplicationRepository.getJobApplicationsByUserIdAndJobId(user.getId(), jobId);
        String cvUrl = this.s3Service.uploadFile(userCv);
        Cosine cosine = this.rsService.cosineSimilarityForOneJob(this.rsService.extractHashtags(userCv), jobDetail);

        if (find == null) {
            JobApplication jobApplication = new JobApplication();
            if (jobDetail != null) {
                jobApplication.setJobId(jobDetail.getId());
                jobApplication.setUserId(user.getId()); 
                jobApplication.setUserCv(cvUrl);
                jobApplication.setMatch(cosine.getMark());
            }
            return this.jobApplicationRepository.save(jobApplication);

        } else {
            if (jobDetail != null) {
                find.setJobId(jobDetail.getId());
                find.setUserId(user.getId());
                find.setUserCv(cvUrl);
                find.setMatch(cosine.getMark());
            }
            return this.jobApplicationRepository.save(find);
        }
    }
}

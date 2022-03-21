package com.jobs.sitran.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobs.sitran.domain.JobApplication;
import com.jobs.sitran.exception.NotFoundException;
import com.jobs.sitran.helper.CSVHelper;
import com.jobs.sitran.model.request.CompanyRequest;
import com.jobs.sitran.model.request.JobDetailRequest;
import com.jobs.sitran.service.JobApplicationService;
import com.jobs.sitran.service.JobDetailService;
import com.jobs.sitran.service.RSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/jobs")
public class JobDetailResource {

    @Autowired
    private JobDetailService jobDetailService;

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private RSService rsService;

    @GetMapping("/panels")
    public ResponseEntity<?> getPanels() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("panels.csv");
        return ResponseEntity.ok().body(CSVHelper.getPanelsFromCSV(is));
    }

    @GetMapping("/hashtags")
    public ResponseEntity<?> getHashtags() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("hashtags.csv");
        return ResponseEntity.ok().body(CSVHelper.getHashtagsFromCSV(is));
    }

    @GetMapping(value = {"/{numOfJobs}/{search}", "/{numOfJobs}/"})
    public ResponseEntity<?> getJobs(@PathVariable Integer numOfJobs, @PathVariable(required = false) String search) {
        return ResponseEntity.ok(this.jobDetailService.getJobs(numOfJobs, search));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createJob(@RequestBody JobDetailRequest jobDetailRequest) throws NotFoundException {
        return ResponseEntity.ok(this.jobDetailService.createJob(jobDetailRequest));
    }

    @GetMapping("/id/{jobId}")
    public ResponseEntity<?> getJobById(@PathVariable(value = "jobId") String jobId,
                                        @RequestHeader(value = "Authorization", required = false) String authorization) throws NotFoundException {
        Map<String, Object> response = new HashMap<>();
        response.put("jobDetail", this.jobDetailService.getJobById(jobId));
        if (authorization != null) {
            response.put("isApplied", this.jobDetailService.userIsApplied(jobId));
        } else {
            response.put("isApplied", null);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recent")
    public ResponseEntity<?> getRecentJobs() {
        return ResponseEntity.ok(this.jobDetailService.getRecentJob());
    }

    @PostMapping(value = "/id/{jobId}", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> applyToJob(@PathVariable(value = "jobId") String jobId, @RequestParam MultipartFile userCv) throws NotFoundException, IOException {
        JobApplication jobApplication = this.jobApplicationService.applyToJob(jobId, userCv);
        return ResponseEntity.ok(jobApplication);
    }

    @PostMapping("/get-related-jobs")
    public ResponseEntity<?> getRelatedJobs(@RequestParam("hashtags") String hashtags, @RequestParam("jobId") String jobId) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Set<String> hashtagArray = mapper.readValue(hashtags, HashSet.class);
        return ResponseEntity.ok(this.jobDetailService.getRelatedJobs(hashtagArray, jobId));
    }

    @PostMapping(value = "/fast-apply/cv", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadCV(@RequestParam MultipartFile cv) throws IOException {
        return ResponseEntity.ok(this.rsService.cosineSimilarity(this.rsService.extractHashtags(cv)));
    }
}

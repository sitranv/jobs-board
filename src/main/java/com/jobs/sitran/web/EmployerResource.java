package com.jobs.sitran.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobs.sitran.domain.Company;
import com.jobs.sitran.domain.JobDetail;
import com.jobs.sitran.domain.subdocument.Candidate;
import com.jobs.sitran.exception.NotFoundException;
import com.jobs.sitran.model.request.CompanyRequest;
import com.jobs.sitran.model.request.JobDetailRequest;
import com.jobs.sitran.service.CompanyService;
import com.jobs.sitran.service.JobDetailService;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employer")
public class EmployerResource {

    @Autowired
    private JobDetailService jobService;

    @Autowired
    private CompanyService companyService;

    @GetMapping("/jobs")
    public ResponseEntity<?> getJobs() throws NotFoundException {
        return ResponseEntity.ok(this.jobService.getAllJobsByEmployer().getContent());
    }

    @PostMapping("/jobs")
    public ResponseEntity<?> createJob(@RequestBody JobDetailRequest jobRequest) throws NotFoundException {
        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("job", this.jobService.createJob(jobRequest));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<?> getJobById(@PathVariable String id) throws NotFoundException {
        JobDetail jobDetail = this.jobService.getJobById(id);
        return ResponseEntity.ok(jobDetail);
    }

    @GetMapping("/jobs/{id}/candidates")
    public ResponseEntity<?> getCandidatesByJobId(@PathVariable String id) throws NotFoundException {
        List<Candidate> candidates = this.jobService.getCandidatesByJobId(id);
        return ResponseEntity.ok(candidates);
    }

    @PutMapping("/jobs/{id}")
    public ResponseEntity<?> updateJob(@PathVariable String id, @RequestBody JobDetailRequest jobRequest) throws NotFoundException {
        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("job", this.jobService.updateJob(id, jobRequest));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable String id) throws NotFoundException {
        Boolean status = this.jobService.deleteJobById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("deletedPostId", id);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/company/update", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateCompany(@RequestParam("companyRequest") String companyRequest, @RequestParam(required = false) MultipartFile companyLogo, @RequestParam(required = false) String companyId) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        CompanyRequest modelDTO = mapper.readValue(companyRequest, CompanyRequest.class);
        Company company = this.companyService.updateCompany(modelDTO, companyLogo, companyId);
        return ResponseEntity.ok(company);
    }

    @GetMapping(value = "/dashboard")
    public ResponseEntity<?> getDashboard() throws NotFoundException {
        return ResponseEntity.ok(this.jobService.dashboard());
    }

    @PatchMapping(value="/jobs/{id}/toggle")
    public ResponseEntity<?> toggleOpening(@PathVariable String id) throws NotFoundException {
        this.jobService.toggle(id);
        return ResponseEntity.ok(true);
    }
}

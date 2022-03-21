package com.jobs.sitran.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobs.sitran.domain.Company;
import com.jobs.sitran.model.request.CompanyRequest;
import com.jobs.sitran.service.CompanyService;
import com.jobs.sitran.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/company")
public class CompanyResource {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private CompanyService companyService;

    @PostMapping(value = "/create", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> createCompany(@RequestParam("companyRequest") String companyRequest, @RequestParam MultipartFile companyLogo) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        CompanyRequest modelDTO = mapper.readValue(companyRequest, CompanyRequest.class);
        Company company = this.companyService.createCompany(modelDTO, companyLogo);
        return ResponseEntity.ok(company);
    }
}

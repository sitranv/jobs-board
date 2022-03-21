package com.jobs.sitran.web;

import com.jobs.sitran.service.CompanyService;
import com.jobs.sitran.service.JobDetailService;
import com.jobs.sitran.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminResource {

    @Autowired
    private JobDetailService jobService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(this.userService.getUsers());
    }

    @GetMapping("/companies")
    public ResponseEntity<?> getCompanies() {
        return ResponseEntity.ok(this.companyService.getCompanies());
    }

    @GetMapping(value = {"/companies/{numOfJobs}/{search}", "/{numOfJobs}/"})
    public ResponseEntity<?> getJobs(@PathVariable Integer numOfJobs, @PathVariable(required = false) String search) {
        return ResponseEntity.ok(this.companyService.getCompanies(numOfJobs, search));
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<?> getCompany(@PathVariable String id) {
        return ResponseEntity.ok(this.companyService.getCompany(id));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getAdminDashboard() {
        return ResponseEntity.ok(this.jobService.getAdminDashboard());
    }

}

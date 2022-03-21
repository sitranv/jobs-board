package com.jobs.sitran.web;

import com.jobs.sitran.domain.Authority;
import com.jobs.sitran.domain.Company;
import com.jobs.sitran.domain.User;
import com.jobs.sitran.exception.ExistException;
import com.jobs.sitran.model.dto.UserDTO;
import com.jobs.sitran.model.request.ChangePasswordRequest;
import com.jobs.sitran.model.request.RegisterRequest;
import com.jobs.sitran.model.response.AppliedJobResponse;
import com.jobs.sitran.model.response.RegisterResponse;
import com.jobs.sitran.repository.CompanyRepository;
import com.jobs.sitran.security.AuthorityConstants;
import com.jobs.sitran.security.jwt.TokenProvider;
import com.jobs.sitran.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) throws ExistException, MessagingException {
        RegisterResponse response = new RegisterResponse();
        response.setUser(this.userService.register(registerRequest));
        response.setMessage("Register successful! Check your email to verify your account.");
        response.setStatus(true);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/confirm")
    public ResponseEntity<?> activateEmail(@RequestParam(value = "token") String token) {
        return ResponseEntity.ok().body(this.userService.activateEmail(token));
    }

    @GetMapping(value = "/profile/get")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authorization) {
        if (authorization.startsWith("Bearer ")) {
            UserDTO user = this.userService.getCurrentUser(authorization);
            Map<String, Object> response = new HashMap<>();
            response.put("user", user);
            if (user.getAuthorities().contains(new Authority(AuthorityConstants.EMPLOYER))) {
                Optional<Company> company = this.userService.getCompanyByEmployerId(user.getId());
                response.put("isCreatedCompany", company.isPresent());
                response.put("company", company);
            } else if (user.getAuthorities().contains(new Authority(AuthorityConstants.USER))) {
                response.put("userAppliedJobs", this.userService.getAppliedJobsId());
            }
            return ResponseEntity.ok(response);
        }
        return new ResponseEntity<>(authorization, HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value= "/profile")
    public ResponseEntity<?> updateProfile(@RequestBody User userRequest) {
        Map<String, Object> response = new HashMap<>();
        User user = this.userService.updateUser(userRequest);
        if (user != null) {
            response.put("success", true);
            response.put("user", this.userService.updateUser(userRequest));
        } else {
            response.put("success", false);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/applied-jobs")
    public ResponseEntity<?> getAppliedJobs(@RequestHeader("Authorization") String authorization) {
        if (authorization.startsWith("Bearer ")) {
            AppliedJobResponse response = this.userService.getAppliedJob();
            return ResponseEntity.ok(response);
        }
        return new ResponseEntity<>(authorization, HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping(value = "/applied-jobs/{jobId}")
    public ResponseEntity<?> deleteApplyJob(@RequestHeader("Authorization") String authorization, @PathVariable String jobId) {
        if (authorization.startsWith("Bearer ")) {
            Boolean response = this.userService.deleteApplyJob(jobId);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(false);
    }

    @PostMapping(value = "/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.ok(this.userService.changePassword(changePasswordRequest));
    }
}

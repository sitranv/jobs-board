package com.jobs.sitran.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jobs.sitran.domain.Authority;
import com.jobs.sitran.domain.Company;
import com.jobs.sitran.domain.User;
import com.jobs.sitran.exception.NotFoundException;
import com.jobs.sitran.model.dto.UserDTO;
import com.jobs.sitran.model.request.LoginRequest;
import com.jobs.sitran.security.AuthorityConstants;
import com.jobs.sitran.security.jwt.JwtFilter;
import com.jobs.sitran.security.jwt.TokenProvider;
import com.jobs.sitran.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class AuthResource {

    private final Logger log = LoggerFactory.getLogger(AuthResource.class);

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private UserService userService;

    public AuthResource(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> authorize(@Valid @RequestBody LoginRequest loginRequest) throws NotFoundException {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Object principal = SecurityContextHolder. getContext(). getAuthentication(). getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Map<String, Object> response = new HashMap<>();

        UserDTO user = this.userService.getUserDTO(username);
        String jwt = tokenProvider.createToken(authentication, true);
        response.put("user", user);
        response.put("token", jwt);
        if (user.getAuthorities().contains(new Authority(AuthorityConstants.EMPLOYER))) {
            Optional<Company> company = this.userService.getCompanyByEmployerId(user.getId());
            response.put("isCreatedCompany", company.isPresent());
            response.put("company", company);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

//    static class JWTToken {
//
//        private String token;
//
//        @JsonProperty("user")
//        private UserDTO user;
//
//        @JsonProperty("isHasCompany")
//        private Boolean isHasCompany;
//
//        JWTToken(String token, UserDTO user, Boolean status) {
//            this.token = token;
//            this.user = user;
//            this.isHasCompany = status;
//        }
//
//        JWTToken(String token) {
//            this.token = token;
//        }
//
//        @JsonProperty("token")
//        String getToken() {
//            return token;
//        }
//
//        void setToken(String token) {
//            this.token = token;
//        }
//    }
}

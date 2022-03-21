package com.jobs.sitran.service;

import com.jobs.sitran.domain.*;
import com.jobs.sitran.exception.ExistException;
import com.jobs.sitran.exception.NotFoundException;
import com.jobs.sitran.model.dto.JobApplicationDTO;
import com.jobs.sitran.model.dto.UserDTO;
import com.jobs.sitran.model.mapper.UserMapper;
import com.jobs.sitran.model.request.ChangePasswordRequest;
import com.jobs.sitran.model.request.RegisterRequest;
import com.jobs.sitran.model.response.ActivateResponse;
import com.jobs.sitran.model.response.AppliedJobResponse;
import com.jobs.sitran.repository.*;
import com.jobs.sitran.security.AuthorityConstants;
import com.jobs.sitran.security.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private JobDetailRepository jobDetailRepository;

    @Value("${user.route.confirm}")
    private String routeConfirm;

    public UserDTO getUserDTO(final String email) {
        return userRepository.findOneByEmailIgnoreCase(email)
                .map(user -> UserMapper.toUserDTO(user))
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " was not found in the database"));
    }

    public UserDTO getUserDTOById(final String id) {
        return userRepository.findUserById(id)
                .map(user -> UserMapper.toUserDTO(user))
                .orElseThrow(() -> new UsernameNotFoundException("User with id " + id + " was not found in the database"));
    }

    public UserDTO register(RegisterRequest registerRequest) throws ExistException, MessagingException {
        Boolean check = this.userRepository.existsByEmail(registerRequest.getEmail());

        if (!check) {
            User user = new User();
            user.setFirstName(registerRequest.getFirstName());
            user.setLastName(registerRequest.getLastName());
            user.setEmail(registerRequest.getEmail());
            String password = passwordEncoder.encode(registerRequest.getPassword());
            user.setPassword(password);
            user.setActivated(false);
            String activationKey = UUID.randomUUID().toString();
            user.setActivationKey(activationKey);
            user.setImageUrl(null);
            user.setExpiredAt(LocalDateTime.now().plusHours(1));
            Set<Authority> authorities = new HashSet<>();
            authorityRepository.findById(registerRequest.getRole()).ifPresent(authorities::add);
            user.setAuthorities(authorities);
            User newUser = userRepository.save(user);
            UserDTO userDTO = UserMapper.toUserDTO(newUser);
            if (registerRequest.getRole().trim().equals(AuthorityConstants.EMPLOYER)) {
                createCompany(user.getId());
            }
            this.mailService.sendMail(userDTO.getEmail(), "Activate your email", mailContent(user.getActivationKey()));
            return userDTO;

        } else {
            throw new ExistException("Email", "is exist");
        }
    }

    private Company createCompany(String userId) {
        Company company = new Company();
        company.setName("");
        company.setCompanyLogo("");
        company.setDescription("");
        company.setEmployerId(userId);
        company.setCompanyAddress("");
        return this.companyRepository.save(company);
    }

    public ActivateResponse activateEmail(String token) {
        User user = this.userRepository.findOneByActivationKey(token).orElseThrow(
                () -> new UsernameNotFoundException("User not found in the database"));

        if (user.getActivationKey() == null) {
            throw new IllegalStateException("Email already confirmed");
        }

        if (user.getExpiredAt() == null || user.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token is expired");
        }

        user.setActivated(true);
        user.setExpiredAt(null);
        this.userRepository.save(user);
        return new ActivateResponse(UserMapper.toUserDTO(user), true);
    }

    private String mailContent(String token) {
        return "Hello,\n" +
                "\n" +
                "You have requested to verify your email.\n" +
                "\n" +
                "This is the code to verify your email: " + token;
    }

    public Optional<Company> getCompanyByEmployerId(String employerId) {
        Optional<Company> company = this.companyRepository.findCompanyByEmployerId(employerId);
        return company;
    }

    public UserDTO getCurrentUser(String token) {
        token = token.substring(7);
        Authentication authentication = this.tokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return getUserDTO(username);
    }

    public UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return getUserDTO(username);
    }

    public AppliedJobResponse getAppliedJob() {
        UserDTO user = getCurrentUser();
        List<JobApplication> appliedJobs = this.jobApplicationRepository.getJobApplicationsByUserIdOrderByCreatedDateDesc(user.getId());
        List<JobApplicationDTO> jobList = new ArrayList<>();
        for (JobApplication appliedJob : appliedJobs) {
            JobDetail job = this.jobDetailRepository.getJobDetailById(appliedJob.getJobId());
            JobApplicationDTO jobApplicationDTO = new JobApplicationDTO();
            jobApplicationDTO.setJob(job);
            jobApplicationDTO.setAppliedDate(appliedJob.getCreatedDate());
            jobApplicationDTO.setUserCV(appliedJob.getUserCv());
            jobApplicationDTO.setId(appliedJob.getId());
            jobList.add(jobApplicationDTO);
        }

        AppliedJobResponse response = new AppliedJobResponse();
        response.setUser(user);
        response.setAppliedJobs(jobList);
        response.setSuccess(true);
        return response;
    }

    public List<JobApplication> getAppliedJobsId() {
        UserDTO user = getCurrentUser();
        List<JobApplication> appliedJobs = this.jobApplicationRepository
                .getJobApplicationsByUserIdOrderByCreatedDateDesc(user.getId());
        return appliedJobs;
    }

    public User updateUser(User userRequest) {
        UserDTO currentUser = getCurrentUser();
        User user = userRepository.findOneByEmailIgnoreCase(userRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + userRequest.getEmail() + " was not found in the database"));
        if (currentUser.getEmail().equals(user.getEmail())) {
            user.setLastName(userRequest.getLastName());
            user.setFirstName(userRequest.getFirstName());
            return this.userRepository.save(user);
        } else {
            return null;
        }
    }

    public List<UserDTO> getUsers() {
        List<User> users = this.userRepository.findAll();
        List<UserDTO> userDTOS = new ArrayList<>();

        for (User user : users) {
            userDTOS.add(UserMapper.toUserDTO(user));
        }
        return userDTOS;
    }

    public Boolean deleteApplyJob(String jobId) {
        UserDTO currentUser = getCurrentUser();
        JobApplication jobApplication = this.jobApplicationRepository.getJobApplicationsByUserIdAndJobId(currentUser.getId(), jobId);
        this.jobApplicationRepository.delete(jobApplication);
        return true;
    }

    public Map<String, Object> changePassword(ChangePasswordRequest changePasswordRequest) {
        UserDTO currentUser = getCurrentUser();
        Map<String, Object> response = new HashMap<>();
        User user = this.userRepository.findByEmail(currentUser.getEmail());
        Boolean status = BCrypt.checkpw(changePasswordRequest.getOldPassword(), user.getPassword());
        if (status) {
            if (changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
                user.setPassword(this.passwordEncoder.encode(changePasswordRequest.getNewPassword()));
                response.put("status", true);
                response.put("user", this.userRepository.save(user));
                return response;
            } else {
                response.put("status", false);
                response.put("message", "Password confirmation does not match");
                return response;
            }
        } else {
            response.put("status", false);
            response.put("message", "Wrong password");
            return response;
        }
    }
}

package com.jobs.sitran.service;

import com.jobs.sitran.domain.Authority;
import com.jobs.sitran.domain.Company;
import com.jobs.sitran.exception.NotFoundException;
import com.jobs.sitran.model.dto.UserDTO;
import com.jobs.sitran.model.request.CompanyRequest;
import com.jobs.sitran.repository.CompanyRepository;
import com.jobs.sitran.repository.JobDetailRepository;
import com.jobs.sitran.security.AuthorityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.management.Query;
import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private UserService userService;

    @Autowired
    private JobDetailRepository jobDetailRepository;

    public Company createCompany(CompanyRequest companyRequest, MultipartFile companyLogo) {
        UserDTO user = this.userService.getCurrentUser();
        if (user.getAuthorities().contains(new Authority(AuthorityConstants.EMPLOYER))) {
            Optional<Company> checkExistCompany = this.companyRepository.findCompanyByEmployerId(user.getId());
            if (!checkExistCompany.isPresent()) {
                Company company = new Company();
                company.setCompanyAddress(companyRequest.getCompanyAddress());
                company.setName(companyRequest.getName());
                company.setDescription(companyRequest.getDescription());
                company.setEmployerId(user.getId());
                String logoUrl = this.s3Service.uploadFile(companyLogo);
                company.setCompanyLogo(logoUrl);
                return this.companyRepository.save(company);
            }
        }
        return new Company();
    }

    public Company updateCompany(CompanyRequest companyRequest, MultipartFile logo, String companyId) {
        UserDTO user = this.userService.getCurrentUser();
        Company company = null;

        if (user.getAuthorities().contains(new Authority(AuthorityConstants.EMPLOYER))) {
            company = this.companyRepository.getCompanyByEmployerId(user.getId());
        } else if (user.getAuthorities().contains(new Authority(AuthorityConstants.ADMIN))) {
            company = this.companyRepository.getCompanyById(companyId);
        }
        if (companyRequest.getName() != null) {
            company.setName(companyRequest.getName());
        }
        if (companyRequest.getCompanyAddress() != null) {
            company.setCompanyAddress(companyRequest.getCompanyAddress());
        }
        if (companyRequest.getDescription() != null) {
            company.setDescription(companyRequest.getDescription());
        }
        if (logo != null) {
            String logoUrl = this.s3Service.uploadFile(logo);
            company.setCompanyLogo(logoUrl);
        }
        this.companyRepository.save(company);
        return company;
    }

    public Page<Company> getCompanies() {
        Page<Company> companies = this.companyRepository.findAll(PageRequest.of(0, 300));
        return companies;
    }

    public Page<Company> getCompanies(Integer numOfJobs, String search) {
        Page<Company> companies = this.companyRepository.findCompanies(search == null ? "" : search.trim(), PageRequest.of(0, numOfJobs));
        return companies;
    }

    public Optional<Company> getCompany(String id) {
        Optional<Company> company = this.companyRepository.findCompanyById(id);
        return company;
    }
}

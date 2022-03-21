package com.jobs.sitran.model.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CompanyRequest {

    @Email
    @Size(min = 5, max = 254)
    private String name;

    @NotNull
    @Size(min = 60, max = 60)
    private String description;

    @Size(max = 50)
    @NotNull
    @NotBlank
    private String companyAddress;

//    private MultipartFile companyLogo;
}

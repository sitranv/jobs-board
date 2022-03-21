package com.jobs.sitran.model.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest {

    @Email
    @Size(min = 5, max = 254)
    private String email;

    @NotNull
    @Size(min = 60, max = 60)
    private String password;

    @Size(max = 50)
    @NotNull
    @NotBlank
    private String firstName;

    @Size(max = 50)
    @NotNull
    @NotBlank
    private String lastName;

    @Size(max = 50)
    @NotNull
    @NotBlank
    private String role;

//    @Size(max = 256)
//    private String imageUrl;

}

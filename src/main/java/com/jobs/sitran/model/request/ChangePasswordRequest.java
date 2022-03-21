package com.jobs.sitran.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ChangePasswordRequest {
    @NotNull
    @Size(min = 60, max = 60)
    private String oldPassword;

    @NotNull
    @Size(min = 60, max = 60)
    private String newPassword;
    @NotNull
    @Size(min = 60, max = 60)
    private String confirmPassword;

}

package com.jobs.sitran.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Required;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobDetailRequest {

    @NotNull
    @Size(min = 5, max = 254)
    private String title;

    private String companyId;

    @NotNull
    @NotBlank
    private String description;

    private Instant deadline;

    @NotNull
    @NotBlank
    private String salaryRange;

    @NotNull
    @NotBlank
    private String benefit;

    @NotNull
    @NotBlank
    private String request;

    private List<String> hashtags;

    private List<String> place;

}

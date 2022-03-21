package com.jobs.sitran.model.response;

import com.jobs.sitran.model.dto.JobApplicationDTO;
import com.jobs.sitran.model.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppliedJobResponse {

    private UserDTO user;

    private List<JobApplicationDTO> appliedJobs;

    private Boolean success;
}

package com.jobs.sitran.domain.subdocument;

import com.jobs.sitran.domain.JobApplication;
import com.jobs.sitran.model.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {

    private UserDTO user;

    private JobApplication detail;
}

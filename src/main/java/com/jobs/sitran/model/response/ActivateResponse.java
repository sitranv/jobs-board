package com.jobs.sitran.model.response;

import com.jobs.sitran.model.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivateResponse {
    private UserDTO userDTO;

    Boolean success;
}

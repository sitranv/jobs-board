package com.jobs.sitran.model.response;

import com.jobs.sitran.model.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterResponse extends AbstractBaseResponse {

    UserDTO user;
}

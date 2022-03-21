package com.jobs.sitran.model.dto;

import com.jobs.sitran.domain.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String id;
//    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String imageUrl;
//    private String activationKey;
    private boolean activated = false;

    private Set<Authority> authorities = new HashSet<>();
}

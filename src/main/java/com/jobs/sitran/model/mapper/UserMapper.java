package com.jobs.sitran.model.mapper;

import com.jobs.sitran.domain.User;
import com.jobs.sitran.model.dto.UserDTO;

public class UserMapper {

    public static UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getImageUrl(),
//                user.getActivationKey(),
                user.isActivated(),
                user.getAuthorities()
        );
    }
}

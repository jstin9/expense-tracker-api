package com.jstn9.expensetracker.util.mapper;

import com.jstn9.expensetracker.dto.auth.UserResponse;
import com.jstn9.expensetracker.models.User;

public class UserMapper {

    public static UserResponse toUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        return userResponse;
    }
}

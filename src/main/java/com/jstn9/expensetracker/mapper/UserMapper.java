package com.jstn9.expensetracker.mapper;

import com.jstn9.expensetracker.dto.auth.UserResponse;
import com.jstn9.expensetracker.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserResponse toUserResponse(User user);
}

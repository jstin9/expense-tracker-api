package com.jstn9.expensetracker.mapper;

import com.jstn9.expensetracker.dto.profile.ProfileResponse;
import com.jstn9.expensetracker.model.Profile;
import org.mapstruct.Mapper;

@Mapper
public interface ProfileMapper {
    ProfileResponse toProfileResponse(Profile profile);
}

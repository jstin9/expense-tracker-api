package com.jstn9.expensetracker.util.mapper;

import com.jstn9.expensetracker.dto.profile.ProfileResponse;
import com.jstn9.expensetracker.models.Profile;

public class ProfileMapper {
    public static ProfileResponse toProfileResponse(Profile profile){
        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.setId(profile.getId());
        profileResponse.setName(profile.getName());
        profileResponse.setBalance(profile.getBalance());
        profileResponse.setMonthSalary(profile.getMonthSalary());
        profileResponse.setCurrencyType(profile.getCurrencyType());
        profileResponse.setCreatedAt(profile.getCreatedAt());
        profileResponse.setUpdatedAt(profile.getUpdatedAt());
        return profileResponse;
    }
}

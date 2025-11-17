package com.jstn9.expensetracker.service;

import com.jstn9.expensetracker.dto.profile.ProfileRequest;
import com.jstn9.expensetracker.dto.profile.ProfileResponse;
import com.jstn9.expensetracker.models.Profile;
import com.jstn9.expensetracker.models.User;
import com.jstn9.expensetracker.repository.ProfileRepository;
import com.jstn9.expensetracker.util.mapper.ProfileMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserService userService;

    public ProfileService(ProfileRepository profileRepository, UserService userService) {
        this.profileRepository = profileRepository;
        this.userService = userService;
    }

    public ProfileResponse getProfile() {
        Profile profile = getCurrentUserProfile();
        return ProfileMapper.toProfileResponse(profile);
    }


    public ProfileResponse updateProfile(ProfileRequest request) {
        Profile profile = getCurrentUserProfile();

        profile.setName(request.getName());
        profile.setBalance(request.getBalance());
        profile.setMonthSalary(request.getMonthSalary());
        profile.setCurrencyType(request.getCurrencyType());
        Profile savedProfile = profileRepository.save(profile);

        return ProfileMapper.toProfileResponse(savedProfile);
    }

    private Profile getCurrentUserProfile(){
        User user = userService.getCurrentUser();
        return profileRepository.findByUser(user)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

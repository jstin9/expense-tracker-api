package com.jstn9.expensetracker.service;

import com.jstn9.expensetracker.dto.profile.ProfileRequest;
import com.jstn9.expensetracker.dto.profile.ProfileResponse;
import com.jstn9.expensetracker.exception.ProfileNotFilledException;
import com.jstn9.expensetracker.models.Profile;
import com.jstn9.expensetracker.models.User;
import com.jstn9.expensetracker.repository.ProfileRepository;
import com.jstn9.expensetracker.util.MapperUtil;
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
        return MapperUtil.toProfileResponse(profile);
    }


    public ProfileResponse updateProfile(ProfileRequest request) {
        Profile profile = getCurrentUserProfile();

        profile.setName(request.getName());
        profile.setBalance(request.getBalance());
        profile.setMonthSalary(request.getMonthSalary());
        profile.setCurrencyType(request.getCurrencyType());
        Profile savedProfile = profileRepository.save(profile);

        return MapperUtil.toProfileResponse(savedProfile);
    }

    public boolean isProfileFilled(){
        User user = userService.getCurrentUser();
        boolean neverUpdated = profileRepository.isProfileNeverUpdated(user.getId());
        if(neverUpdated){
            throw new ProfileNotFilledException();
        }
        return true;
    }

    private Profile getCurrentUserProfile(){
        User user = userService.getCurrentUser();
        return profileRepository.findByUser(user)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

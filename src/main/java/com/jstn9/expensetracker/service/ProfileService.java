package com.jstn9.expensetracker.service;

import com.jstn9.expensetracker.dto.profile.ProfileRequest;
import com.jstn9.expensetracker.dto.profile.ProfileResponse;
import com.jstn9.expensetracker.exception.ProfileNotFilledException;
import com.jstn9.expensetracker.exception.ProfileNotFoundException;
import com.jstn9.expensetracker.models.Profile;
import com.jstn9.expensetracker.models.User;
import com.jstn9.expensetracker.repository.ProfileRepository;
import com.jstn9.expensetracker.util.MapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserService userService;

    public ProfileService(ProfileRepository profileRepository, UserService userService) {
        this.profileRepository = profileRepository;
        this.userService = userService;
    }

    public ProfileResponse getProfile() {
        User user = userService.getCurrentUser();
        log.info("Profile requested for userId: {}, username: {}",
                user.getId(), user.getUsername());
        Profile profile = getCurrentUserProfile(user);
        return MapperUtil.toProfileResponse(profile);
    }

    public ProfileResponse updateProfile(ProfileRequest request) {
        User user = userService.getCurrentUser();
        log.info("Start updating user profile with userId: {}, username: {}",
                user.getId(), user.getUsername());
        Profile profile = getCurrentUserProfile(user);

        profile.setName(request.getName());
        profile.setBalance(request.getBalance());
        profile.setMonthSalary(request.getMonthSalary());
        profile.setCurrencyType(request.getCurrencyType());
        Profile savedProfile = profileRepository.save(profile);

        log.info("Profile updated successfully for userId: {}, username: {}",
                user.getId(), user.getUsername());
        return MapperUtil.toProfileResponse(savedProfile);
    }

    public boolean isProfileFilled(){
        User user = userService.getCurrentUser();
        log.debug("Checking if profile is filled for userId: {}, username: {}", user.getId(), user.getUsername());
        boolean neverUpdated = profileRepository.isProfileNeverUpdated(user.getId());
        if(neverUpdated){
            log.warn("Profile is not filled for userId: {}, username: {}",
                    user.getId(), user.getUsername());
            throw new ProfileNotFilledException();
        }
        return true;
    }

    private Profile getCurrentUserProfile(User user){
        return profileRepository.findByUser(user)
                .orElseThrow(() -> {
                    log.error("Profile not found for userId: {}, username: {}",
                            user.getId(), user.getUsername());
                    return new ProfileNotFoundException();
                });
    }
}

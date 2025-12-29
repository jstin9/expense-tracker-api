package com.jstn9.expensetracker.service;

import com.jstn9.expensetracker.dto.profile.ProfileRequest;
import com.jstn9.expensetracker.dto.profile.ProfileResponse;
import com.jstn9.expensetracker.exception.ProfileNotFilledException;
import com.jstn9.expensetracker.exception.ProfileNotFoundException;
import com.jstn9.expensetracker.mapper.ProfileMapper;
import com.jstn9.expensetracker.model.Profile;
import com.jstn9.expensetracker.model.User;
import com.jstn9.expensetracker.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserService userService;
    private final ProfileMapper profileMapper;

    public ProfileService(ProfileRepository profileRepository, UserService userService, ProfileMapper profileMapper) {
        this.profileRepository = profileRepository;
        this.userService = userService;
        this.profileMapper = profileMapper;
    }

    public ProfileResponse getProfile() {
        User user = userService.getCurrentUser();
        log.info("Profile requested for userId: {}, username: {}",
                user.getId(), user.getUsername());
        Profile profile = getCurrentUserProfile(user);
        return profileMapper.toProfileResponse(profile);
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
        return profileMapper.toProfileResponse(savedProfile);
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

package com.jstn9.expensetracker.controller;

import com.jstn9.expensetracker.dto.profile.ProfileRequest;
import com.jstn9.expensetracker.dto.profile.ProfileResponse;
import com.jstn9.expensetracker.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping()
    public ResponseEntity<ProfileResponse> getProfile(){
        return ResponseEntity.ok(profileService.getProfile());
    }

    @PutMapping()
    public ResponseEntity<ProfileResponse> updateProfile(@Valid @RequestBody ProfileRequest request){
        return ResponseEntity.ok(profileService.updateProfile(request));
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkProfile(Authentication authentication){

        boolean isFilled = false;

        if(authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)){
            isFilled = profileService.isProfileFilled();
        }

        return ResponseEntity.ok(Map.of("isFilled", isFilled));

    }

}

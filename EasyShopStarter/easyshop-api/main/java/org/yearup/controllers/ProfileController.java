package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;

import java.security.Principal;

@RestController
@RequestMapping("/profile")
@CrossOrigin
public class ProfileController {
    private ProfileDao profileDao;
    private UserDao userDao;

    @Autowired
    public ProfileController(ProfileDao profileDao, UserDao userDao) {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    @GetMapping
    public ResponseEntity<Profile> getProfile(Principal principal) {
        String username = principal.getName();
        int userId = userDao.getIdByUsername(username);
        return ResponseEntity.ok(profileDao.getByUserId(userId));
    }

    @PutMapping
    public ResponseEntity<Profile> updateProfile(@RequestBody Profile profile, Principal principal) {
        String username = principal.getName();
        int userId = userDao.getIdByUsername(username);
        profile.setUserId(userId);

        // Call the DAO's update method (returns void)
        profileDao.update(userId, profile);

        // Fetch the updated profile
        Profile updatedProfile = profileDao.getByUserId(userId);

        return ResponseEntity.ok(updatedProfile);
    }

}
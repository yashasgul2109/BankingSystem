
package org.example.controller;

import org.example.dto.EditProfileRequest;
import org.example.dto.UserProfileResponse;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/{userId}/profile")
    public UserProfileResponse editProfile(
            @PathVariable Long userId,
            @RequestBody EditProfileRequest request) {

        return userService.editProfile(userId, request);
    }
    
@GetMapping("/{userId}/profile")
public UserProfileResponse viewProfile(@PathVariable Long userId) {
    return userService.viewProfile(userId);
}

}

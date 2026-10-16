
package org.example.service;

import org.example.dto.EditProfileRequest;
import org.example.dto.UserProfileResponse;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfileResponse editProfile(Long userId, EditProfileRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getName() == null || request.getName().isBlank()) {
            throw new RuntimeException("Name cannot be empty");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new RuntimeException("Email cannot be empty");
        }

        if (!request.getEmail().equalsIgnoreCase(user.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already registered");
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        User savedUser = userRepository.save(user);

        UserProfileResponse response = new UserProfileResponse();
        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());

        return response;
    }
    
public UserProfileResponse viewProfile(Long userId) {

    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    UserProfileResponse response = new UserProfileResponse();
    response.setId(user.getId());
    response.setName(user.getName());
    response.setEmail(user.getEmail());

    return response;
}

}

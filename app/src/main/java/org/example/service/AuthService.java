package org.example.service;

import org.example.dto.AuthResponse;
import org.example.dto.EmailRequest;
import org.example.dto.LoginRequest;
import org.example.dto.RefreshTokenRequest;
import org.example.dto.SignupRequest;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RestTemplate restTemplate;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RestTemplate restTemplate
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.restTemplate = restTemplate;
    }

    public User signup(SignupRequest request) {

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        User savedUser = userRepository.save(user);

        String url = "http://localhost:3000/send-email";

        String message = "Welcome " + savedUser.getName()
                + ", your Banking System account has been created successfully.";

        EmailRequest emailRequest = new EmailRequest(
                savedUser.getEmail(),
                "Welcome to Banking System",
                message
        );

        restTemplate.postForObject(
                url,
                emailRequest,
                String.class
        );

        return savedUser;
    }

    public AuthResponse login(LoginRequest request) {

        Optional<User> userOptional =
                userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new RuntimeException("Invalid password");
        }

        String accessToken =
                jwtService.generateAccessToken(user.getEmail());

        String refreshToken =
                jwtService.generateRefreshToken(user.getEmail());

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {

        String refreshToken = request.getRefreshToken();

        if (!jwtService.isTokenValid(refreshToken)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        String email = jwtService.extractEmail(refreshToken);

        String newAccessToken =
                jwtService.generateAccessToken(email);

        return new AuthResponse(
                newAccessToken,
                refreshToken
        );
    }
}
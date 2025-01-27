package com.alikuslu.housefit.demo.service;

import com.alikuslu.housefit.demo.dto.LoginUserDto;
import com.alikuslu.housefit.demo.dto.RegisterUserDto;
import com.alikuslu.housefit.demo.exception.AuthenticationException;
import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.model.UserType;
import com.alikuslu.housefit.demo.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User signup(RegisterUserDto input) {
        if (userRepository.findByPhoneNumber(input.getPhoneNumber()).isPresent()) {
            throw new AuthenticationException(String.format("User with username %s already exists!", input.getPhoneNumber()), "username");
        }

        String referralCode = generateReferralCode();

        User user = User.builder()
                .name(input.getName())
                .surname(input.getSurname())
                .phoneNumber(input.getPhoneNumber())
                .password(passwordEncoder.encode(input.getPassword()))
                .referralCode(referralCode)
                .userType(UserType.CUSTOMER)
                .points(0.0)
                .build();

        if (input.getReferralCode() != null && !input.getReferralCode().isEmpty()) {
            User referrer = userRepository.findByReferralCode(input.getReferralCode());
            if (referrer != null) {
                referrer.setPoints(referrer.getPoints() + 10);
                user.setPoints(user.getPoints() + 5);
                user.setReferredBy(referrer.getReferralCode());
                userRepository.save(referrer);
            }
        }

        return userRepository.save(user);
    }

    /*
    @PreAuthorize("hasRole('TRAINER')")
    public void acceptAppointment(Long appointmentId) {
        // Logic for accepting an appointment
    }
    */

    public User authenticate(LoginUserDto input) {
        User user = userRepository.findByUsername(input.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        return user;
    }

    public User authenticateByPhone(LoginUserDto input) {
        User user = userRepository.findByPhoneNumber(input.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getPhoneNumber(),
                        input.getPassword()
                )
        );

        return user;
    }

    private String generateReferralCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}

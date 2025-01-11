package com.alikuslu.housefit.demo.service;

import com.alikuslu.housefit.demo.dto.RegisterUserDto;
import com.alikuslu.housefit.demo.exception.AuthenticationException;
import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.model.UserType;
import com.alikuslu.housefit.demo.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        if (userRepository.findByUsername(input.getUsername()).isPresent()) {
            throw new AuthenticationException(String.format("User with username %s already exists!", input.getUsername()), "username");
        }

        User user = User.builder()
                .username(input.getUsername())
                .password(passwordEncoder.encode(input.getPassword()))
                .userType(UserType.CUSTOMER)
                .points(0.0)
                .build();

        return userRepository.save(user);
    }

    /*
    @PreAuthorize("hasRole('TRAINER')")
    public void acceptAppointment(Long appointmentId) {
        // Logic for accepting an appointment
    }
    */

    public User authenticate(RegisterUserDto input) {
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
}

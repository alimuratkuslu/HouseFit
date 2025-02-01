package com.alikuslu.housefit.demo.service;

import com.alikuslu.housefit.demo.dto.LoginUserDto;
import com.alikuslu.housefit.demo.dto.RegisterUserDto;
import com.alikuslu.housefit.demo.exception.AuthenticationException;
import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.model.UserType;
import com.alikuslu.housefit.demo.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
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

    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String resetCode = generateResetCode();
        user.setPasswordResetCode(resetCode);
        user.setPasswordResetCodeExpiresAt(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        sendPasswordResetEmail(user, resetCode);
    }

    public void verifyResetCode(String email, String code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!code.equals(user.getPasswordResetCode())) {
            throw new RuntimeException("Invalid reset code");
        }

        if (LocalDateTime.now().isAfter(user.getPasswordResetCodeExpiresAt())) {
            throw new RuntimeException("Reset code has expired");
        }
    }

    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetCode(null);
        user.setPasswordResetCodeExpiresAt(null);
        userRepository.save(user);
    }

    private String generateResetCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }

    private void sendPasswordResetEmail(User user, String code) {
        String subject = "Password Reset Request";
        String htmlMessage = String.format("""
            <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Password Reset | HouseFit</title>
                </head>
                <body style="margin: 0; padding: 0; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;">
                    <div style="background: #f8f9fa; padding: 40px 0;">
                        <div style="max-width: 600px; margin: 0 auto; background: #ffffff; border-radius: 16px; box-shadow: 0 4px 24px rgba(0,0,0,0.08);">
                            <div style="background: linear-gradient(135deg, #6366f1 0%%, #a855f7 100%%); padding: 32px; border-radius: 16px 16px 0 0; text-align: center;">
                                <h1 style="color: #ffffff; margin: 0; font-size: 28px; font-weight: 600;">Password Reset Request</h1>
                            </div>
            
                            <div style="padding: 40px 32px;">
                                <div style="text-align: center; margin-bottom: 32px;">
                                    <div style="background: #f1f5f9; border-radius: 50%%; width: 80px; height: 80px; margin: 0 auto 24px; display: flex; align-items: center; justify-content: center;">
                                        <svg style="width: 36px; height: 36px; color: #6366f1;" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z"></path>
                                        </svg>
                                    </div>
                                    <h2 style="color: #1e293b; margin: 0 0 16px; font-size: 22px; font-weight: 600;">Your Security Code</h2>
                                    <p style="color: #64748b; margin: 0; line-height: 1.6;">Use this verification code to reset your password:</p>
                                </div>
            
                                <div style="background: #f8fafc; border-radius: 12px; padding: 24px; text-align: center; margin-bottom: 32px;">
                                    <div style="display: inline-block; background: #ffffff; padding: 12px 24px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.05);">
                                        <div style="font-size: 32px; font-weight: 700; letter-spacing: 2px; color: #6366f1; font-family: monospace;">%s</div>
                                    </div>
                                </div>
            
                                <div style="text-align: center; margin-bottom: 32px;">
                                    <div style="display: inline-flex; align-items: center; background: #fff4e6; padding: 12px 20px; border-radius: 8px; margin-bottom: 24px;">
                                        <svg style="width: 20px; height: 20px; color: #ff922b; margin-right: 12px;" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                                        </svg>
                                        <p style="color: #ff922b; margin: 0; font-weight: 500;">Code expires in 1 hour</p>
                                    </div>
                                    <p style="color: #64748b; margin: 0 0 24px; line-height: 1.6;">If you didn't request this code, you can safely ignore this email.</p>
                                </div>
                            </div>
            
                            <div style="background: #f8f9fa; padding: 24px; border-radius: 0 0 16px 16px; text-align: center;">
                                <p style="color: #64748b; margin: 0 0 12px; font-size: 14px;">Need help? Contact our <a href="[Support-Link]" style="color: #6366f1; text-decoration: none;">support team</a></p>
                                <div style="display: flex; gap: 16px; justify-content: center;">
                                    <a href="[Website-Link]" style="color: #6366f1; text-decoration: none; font-size: 14px;">Visit Website</a>
                                    <span style="color: #cbd5e1;">•</span>
                                    <a href="[Privacy-Link]" style="color: #6366f1; text-decoration: none; font-size: 14px;">Privacy Policy</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </body>
            </html>
        """, code);

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send password reset email");
        }
    }

    private String generateReferralCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}

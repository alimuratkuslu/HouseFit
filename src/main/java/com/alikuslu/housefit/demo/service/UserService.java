package com.alikuslu.housefit.demo.service;

import com.alikuslu.housefit.demo.dto.NotificationSettingsDto;
import com.alikuslu.housefit.demo.dto.UpdateMeasurementDto;
import com.alikuslu.housefit.demo.dto.UpdateProfileDto;
import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.model.UserType;
import com.alikuslu.housefit.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> searchUsers(String query, UserType type) {
        return userRepository.searchByNameOrSurnameAndType(query, type);
    }

    public List<User> getAllTrainers() {
        return userRepository.findByUserType(UserType.TRAINER);
    }

    public User updateProfile(Long id, UpdateProfileDto profileDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(profileDto.getName());
        user.setSurname(profileDto.getSurname());
        user.setDateOfBirth(profileDto.getDateOfBirth());

        return userRepository.save(user);
    }

    public User updateMeasurements(Long id, UpdateMeasurementDto measurementsDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setWeight(measurementsDto.getWeight());
        user.setHeight(measurementsDto.getHeight());
        user.setBodyFat(measurementsDto.getBodyFat());
        user.setMuscleMass(measurementsDto.getMuscleMass());

        return userRepository.save(user);
    }

    public User updateNotificationSettings(Long id, NotificationSettingsDto settingsDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmailNotifications(settingsDto.getEmailNotifications());
        user.setPushNotifications(settingsDto.getPushNotifications());
        user.setReminderTime(settingsDto.getReminderTime());

        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return optionalUser.get();
    }

    public User findByPhoneNumber(String phoneNumber) {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return optionalUser.get();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User updateUsername(Long userId, String newUsername) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(newUsername);
        return userRepository.save(user);
    }

    public User updatePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    public User updateAvatar(Long userId, String avatarUrl) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setAvatar(avatarUrl);
        return userRepository.save(user);
    }
}

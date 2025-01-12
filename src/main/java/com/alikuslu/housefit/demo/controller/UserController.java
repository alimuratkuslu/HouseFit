package com.alikuslu.housefit.demo.controller;

import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/trainers")
    public ResponseEntity<List<User>> getAllTrainers() {
        return ResponseEntity.ok(userService.getAllTrainers());
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @PutMapping("/{id}/username")
    public ResponseEntity<User> changeUsername(@PathVariable Long id, @RequestParam String newUsername) {
        User updatedUser = userService.updateUsername(id, newUsername);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<User> changePassword(@PathVariable Long id, @RequestParam String newPassword) {
        User updatedUser = userService.updatePassword(id, newPassword);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{id}/avatar")
    public ResponseEntity<User> changeAvatar(@PathVariable Long id, @RequestParam String avatarUrl) {
        User updatedUser = userService.updateAvatar(id, avatarUrl);
        return ResponseEntity.ok(updatedUser);
    }
}

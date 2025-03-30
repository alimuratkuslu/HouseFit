package com.alikuslu.housefit.demo.controller;

import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.service.S3Service;
import com.alikuslu.housefit.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/user/avatar")
public class UserAvatarController {

    private final S3Service s3Service;
    private final UserService userService;

    public UserAvatarController(S3Service s3Service, UserService userService) {
        this.s3Service = s3Service;
        this.userService = userService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest().body("Only image files are allowed");
        }

        User user = (User) authentication.getPrincipal();
        String avatarUrl = s3Service.updateAvatar(user.getAvatar(), file);

        user.setAvatar(avatarUrl);
        User updatedUser = userService.updateUser(user);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAvatar(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        s3Service.deleteFile(user.getAvatar());

        user.setAvatar(null);
        userService.saveUser(user);

        return ResponseEntity.ok("Avatar deleted successfully");
    }
}

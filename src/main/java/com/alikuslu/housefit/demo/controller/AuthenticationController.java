package com.alikuslu.housefit.demo.controller;

import com.alikuslu.housefit.demo.dto.LoginResponse;
import com.alikuslu.housefit.demo.dto.LoginUserDto;
import com.alikuslu.housefit.demo.dto.RegisterUserDto;
import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.service.AuthenticationService;
import com.alikuslu.housefit.demo.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto){
        User authenticatedUser;

        if (loginUserDto.getUsername().matches("\\d{10}")) {
            authenticatedUser = authenticationService.authenticateByPhone(loginUserDto);
        } else {
            authenticatedUser = authenticationService.authenticate(loginUserDto);
        }
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }
}

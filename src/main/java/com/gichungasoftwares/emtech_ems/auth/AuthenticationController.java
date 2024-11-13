package com.gichungasoftwares.emtech_ems.auth;

import jakarta.validation.Valid;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest regRequest
    ) throws MessagingException {
        authService.register(regRequest);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest loginRequest
    ) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

}

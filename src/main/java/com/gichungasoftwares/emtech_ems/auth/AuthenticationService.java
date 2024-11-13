package com.gichungasoftwares.emtech_ems.auth;

import com.gichungasoftwares.emtech_ems.role.RoleRepository;
import com.gichungasoftwares.emtech_ems.security.JwtService;
import com.gichungasoftwares.emtech_ems.user.User;
import com.gichungasoftwares.emtech_ems.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public void register(RegistrationRequest regRequest) throws MessagingException {
        // check if role user exists before creating a user
        var userRole = roleRepository.findByName("USER")

                .orElseThrow(() -> new IllegalStateException("ROLE USER was not found!"));

        // check if email is already in use
        if (userRepository.existsByEmail(regRequest.getEmail())) {
            throw new MessagingException("Error: Email is already in use!");
        }

        // create a user object
        var user = User.builder()
                .firstname(regRequest.getFirstname())
                .lastname(regRequest.getLastname())
                .email(regRequest.getEmail())
                .password(passwordEncoder.encode(regRequest.getPassword()))
                .isAccountLocked(false)
                .isEnabled(false)
                .roles(List.of(userRole))
                .joinDate(new Date())
                .status("Pending")
                .build();

        // save the user
        userRepository.save(user);

    }

    // sign in user
    public LoginResponse login(LoginRequest loginRequest) {
        // let spring security handle the authentication process including checking password matching
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        var claims = new HashMap<String, Object>();
        var user = ((User)auth.getPrincipal()); // cast the authenticated principal to User object
        claims.put("fullName", user.fullName());

        var jwtToken = jwtService.generateToken(claims, user);
        return LoginResponse.builder().token(jwtToken).build();
    }



}

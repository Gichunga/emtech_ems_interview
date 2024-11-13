package com.gichungasoftwares.emtech_ems.config;

import com.gichungasoftwares.emtech_ems.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<Integer> {

    @Override
    public Optional<Integer> getCurrentAuditor() {
        // check if user is authenticated
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // check if user is authenticated
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        // cast the principal to user object
        User userPrincipal = (User) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal.getUser_Id()); // since the user is identified with id
    }
}

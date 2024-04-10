package com.example.project_sem4_springboot_api.auditing;
import com.example.project_sem4_springboot_api.entities.User;
import com.example.project_sem4_springboot_api.security.service.UserDetailsImpl;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken
        ) {
            return Optional.empty();
        }

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal.getId());
    }
}

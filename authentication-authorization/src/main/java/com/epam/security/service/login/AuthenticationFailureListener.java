package com.epam.security.service.login;

import com.epam.security.user.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final LoginAttemptService loginAttemptService;
    private final UserRepository userRepository;

    AuthenticationFailureListener(LoginAttemptService loginAttemptService,
                                  UserRepository userRepository) {
        this.loginAttemptService = loginAttemptService;
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
        Object principal = e.getAuthentication().getPrincipal();
        if (principal instanceof String) {
            String username = (String) principal;
            if (this.userRepository.findByUsername(username) != null) {
                this.loginAttemptService.loginFailed(username);
            }
        }
    }
}

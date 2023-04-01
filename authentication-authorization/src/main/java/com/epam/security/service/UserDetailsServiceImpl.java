package com.epam.security.service;

import com.epam.security.service.login.LoginAttemptService;
import com.epam.security.user.User;
import com.epam.security.user.UserRepository;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author Dominik_Janiga
 */
@Component
class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    UserDetailsServiceImpl(UserRepository userRepository, LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundUser = this.userRepository.findByUsername(username);
        if (foundUser == null) {
            throw new UsernameNotFoundException("User not found!");
        } else {
            if (loginAttemptService.isBlocked(username)) {
                throw new LockedException("User is blocked");
            }
        }
        String[] roles = foundUser.getUserRoles().split(";");

        return org.springframework.security.core.userdetails.User.withUsername(foundUser.getUsername())
                .password(foundUser.getPassword())
                .roles(roles)
                .build();
    }
}

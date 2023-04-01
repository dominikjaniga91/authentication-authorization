package com.epam.security.controller;

import com.epam.security.service.login.LoginAttemptService;
import com.epam.security.user.User;
import com.epam.security.user.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Dominik_Janiga
 */
@Controller
class ResourcesController {

    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    ResourcesController(UserRepository userRepository, LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
    }

    @GetMapping("/info")
    String info() {
        return "info";
    }

    @GetMapping("/about")
    String about() {
        return "about";
    }

    @GetMapping("/admin")
    String admin() {
        return "admin";
    }

    @GetMapping("/blocked")
    public String blocked(Model model) {
        List<User> users = this.userRepository.findAll();
        Map<String, LocalDateTime> blockedUsers = users.stream()
                .map(User::getUsername)
                .filter(this.loginAttemptService::isBlocked)
                .collect(Collectors.toMap(Function.identity(),
                        u -> this.loginAttemptService.getCachedValue(u).getBlockedTimestamp()));
        if (!blockedUsers.isEmpty()) {
            model.addAttribute("blockedUsers", blockedUsers);
        }
        return "blocked";
    }
}

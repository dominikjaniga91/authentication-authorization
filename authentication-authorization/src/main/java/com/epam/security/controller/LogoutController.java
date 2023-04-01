package com.epam.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Dominik_Janiga
 */
@Controller
class LogoutController {

    @GetMapping("/logoutSuccess")
    String logoutSuccess() {
        return "logout";
    }
}

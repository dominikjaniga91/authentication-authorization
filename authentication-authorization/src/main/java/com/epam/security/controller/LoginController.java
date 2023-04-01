package com.epam.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Dominik_Janiga
 */
@Controller
class LoginController {

    @GetMapping("/login")
    String login() {
        return "login";
    }
}

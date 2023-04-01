package com.epam.security.controller;

import com.epam.security.service.login.LoginAttemptService;
import com.epam.security.user.User;
import com.epam.security.user.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Login controller tests.
 *
 * @author Dominik_Janiga
 */
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginAttemptService loginAttemptService;


    @Test
    void shouldReturnLoginPage_afterSendingGetRequest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));

    }

    @Test
    void shouldAuthorizeUser_afterRequestingLoginWithValidCredentials() throws Exception {
        //given
        User user = new User("user", "$2a$12$Wgrhk6NLRCiDJc1dhdqqce1Q/8zrxdJDn0GsWO24eVEcOua29VQ.K", "VIEW_INFO");
        this.userRepository.save(user);

        //then /login?error
        mockMvc.perform(formLogin().user("user").password("pass"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("user"));
    }

    @Test
    void shouldUnauthorizedUser_afterRequestingLoginWithInvalidCredentials() throws Exception {
        //given
        User user = new User("unauthenticated", "pass", "VIEW_INFO");
        this.userRepository.save(user);

        //then
        mockMvc.perform(formLogin().user("unauthenticated").password("wrong_password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated())
                .andExpect(request().sessionAttribute("SPRING_SECURITY_LAST_EXCEPTION", "Bad credentials"));
    }

    @Test
    void shouldUnauthorizedUser_afterRequestingLoginWithBlockedUser() throws Exception {
        //given
        User user = new User("blocked_user_login", "$2a$12$Wgrhk6NLRCiDJc1dhdqqce1Q/8zrxdJDn0GsWO24eVEcOua29VQ.K", "VIEW_INFO");
        this.userRepository.save(user);

        this.loginAttemptService.loginFailed("blocked_user_login");
        this.loginAttemptService.loginFailed("blocked_user_login");
        this.loginAttemptService.loginFailed("blocked_user_login");
        this.loginAttemptService.loginFailed("blocked_user_login");

        //then
        mockMvc.perform(formLogin().user("blocked_user_login").password("pass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated())
                .andExpect(request().sessionAttribute("SPRING_SECURITY_LAST_EXCEPTION", "User is blocked"));
    }
}

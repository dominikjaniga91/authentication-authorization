package com.epam.security.controller;

import com.epam.security.service.login.CacheValue;
import com.epam.security.service.login.LoginAttemptService;
import com.epam.security.user.User;
import com.epam.security.user.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Resource controller tests.
 *
 * @author Dominik_Janiga
 */
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class ResourcesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "user", roles = "VIEW_INFO")
    void shouldReturnInfoPage_afterRequestingInfoEndpointWithRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/info"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));

    }

    @Test
    void shouldReturnAboutPage_afterRequestingAboutEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/about"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));

    }

    @Test
    @WithMockUser(username = "user", roles = "VIEW_ADMIN")
    void shouldReturnAdminPage_afterRequestingAdminEndpointWithRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));

    }

    @Test
    @WithMockUser(username = "blocked_user", roles = "VIEW_ADMIN")
    void shouldReturnBlockedUsersPage_afterRequestingBlockedEndpoint() throws Exception {
        //given
        User user = new User("blocked_user", "pass", "VIEW_ADMIN");
        this.userRepository.save(user);

        //when
        this.loginAttemptService.loginFailed("blocked_user");
        this.loginAttemptService.loginFailed("blocked_user");
        this.loginAttemptService.loginFailed("blocked_user");
        this.loginAttemptService.loginFailed("blocked_user");

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/blocked"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));

        CacheValue cachedValue = this.loginAttemptService.getCachedValue("blocked_user");
        Assertions.assertEquals(4, cachedValue.getAttempts());
    }

    @Test
    @WithMockUser(username = "user")
    void shouldReturnStatusForbidden_afterRequestingInfoEndpointWithoutRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/info"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));
    }
}

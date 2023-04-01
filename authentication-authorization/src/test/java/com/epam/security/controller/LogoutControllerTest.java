package com.epam.security.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Logout controller tests.
 *
 * @author Dominik_Janiga
 */
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class LogoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAccessLogout_afterRequestingLogoutEndpoint() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));

    }

    @Test
    void shouldRedirectToAppropriateEndpoint_afterPerformingLogout() throws Exception {
        mockMvc.perform(logout())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/logoutSuccess"));
    }

    @Test
    void shouldReturnLogoutPage_afterRequestingLogoutSuccessEndpoint() throws Exception {
        mockMvc.perform(get("/logoutSuccess"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));
    }
}

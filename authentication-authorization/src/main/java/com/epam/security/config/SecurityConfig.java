package com.epam.security.config;

import com.epam.security.user.Roles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author Dominik_Janiga
 */
@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationFailureHandler authenticationFailureHandler) throws Exception {
        return http.authorizeRequests(authorizeRequests ->
                authorizeRequests
                        .antMatchers(HttpMethod.GET, "/about", "/login*", "/blocked", "/css/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/info").hasRole(Roles.VIEW_INFO.toString())
                        .antMatchers(HttpMethod.GET, "/admin").hasRole(Roles.VIEW_ADMIN.toString())
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin.loginPage("/login")
                        .failureHandler(authenticationFailureHandler)
                        .permitAll()
                ).logout(formLogout -> formLogout
                        .deleteCookies("JSESSIONID")
                        //if CSRF is enabled then logout should be POST request of redefined using AntPathRequestMatcher
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/logoutSuccess")
                        .permitAll()
                ).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

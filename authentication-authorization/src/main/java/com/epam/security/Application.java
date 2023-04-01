package com.epam.security;

import com.epam.security.user.User;
import com.epam.security.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

/**
 * @author Dominik_Janiga
 */
@SpringBootApplication
class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner startUp(UserRepository userRepository) {
        LOGGER.debug("Saving users on application startup");
        return args -> {
            User infoUser = new User("infouser", "$2a$12$Wgrhk6NLRCiDJc1dhdqqce1Q/8zrxdJDn0GsWO24eVEcOua29VQ.K", "VIEW_INFO");
            User adminUser = new User("adminuser", "$2a$12$Wgrhk6NLRCiDJc1dhdqqce1Q/8zrxdJDn0GsWO24eVEcOua29VQ.K", "VIEW_ADMIN");
            userRepository.saveAll(Arrays.asList(infoUser, adminUser));
        };
    }
}

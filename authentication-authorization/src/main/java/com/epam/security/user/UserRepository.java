package com.epam.security.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Dominik_Janiga
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}

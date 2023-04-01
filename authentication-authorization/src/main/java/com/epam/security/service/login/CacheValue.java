package com.epam.security.service.login;

import java.time.LocalDateTime;

/**
 * @author Dominik_Janiga
 */
public class CacheValue {

    private int attempts;
    private LocalDateTime blockedTimestamp;

    public CacheValue(int attempts) {
        this.attempts = attempts;
    }

    public CacheValue(int attempts, LocalDateTime blockedTimestamp) {
        this.attempts = attempts;
        this.blockedTimestamp = blockedTimestamp;
    }

    public int getAttempts() {
        return attempts;
    }

    public LocalDateTime getBlockedTimestamp() {
        return blockedTimestamp;
    }

    public void incrementAttempts() {
        this.attempts++;
    }

    public void setBlockedTimestamp(LocalDateTime blockedTimestamp) {
        this.blockedTimestamp = blockedTimestamp;
    }

    public void restoreAttempts() {
        this.attempts = 0;
    }
}

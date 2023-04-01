package com.epam.security.service.login;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    public static final int MAX_ATTEMPT = 3;
    public static final int BLOCK_DURATION_SEC = 40;
    private final LoadingCache<String, CacheValue> attemptsCache;

    public LoginAttemptService() {
        super();
        attemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(BLOCK_DURATION_SEC, TimeUnit.SECONDS)
                .build(new CacheLoader<String, CacheValue>() {
                    @Override
                    public CacheValue load(final String key) {
                        return new CacheValue(0, LocalDateTime.now());
                    }
                });
    }

    public void loginFailed(final String key) {
        CacheValue cacheValue = new CacheValue(0);
        try {
            cacheValue = attemptsCache.get(key);
            cacheValue.incrementAttempts();
        } catch (final ExecutionException e) {
            cacheValue.restoreAttempts();
        }

        if (isBlocked(key) && cacheValue.getBlockedTimestamp() == null) {
            cacheValue.setBlockedTimestamp(LocalDateTime.now());
        }
        attemptsCache.put(key, cacheValue);
    }

    public boolean isBlocked(String key) {
        try {
            CacheValue cacheValue = attemptsCache.get(key);
            return cacheValue.getAttempts() >= MAX_ATTEMPT;
        } catch (final ExecutionException e) {
            return false;
        }
    }

    public CacheValue getCachedValue(String username) {
        return this.attemptsCache.getUnchecked(username);
    }
}

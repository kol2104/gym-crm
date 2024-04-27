package com.epam.gymcrm.service.security;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    private static final  int MAX_ATTEMPT = 3;
    private static final long LOCK_TIME_MS = TimeUnit.MINUTES.toMillis(5);
    private final ConcurrentHashMap<String, Integer> attemptsCache;
    private final ConcurrentHashMap<String, Long> lockoutCache;

    public LoginAttemptService() {
        this.attemptsCache = new ConcurrentHashMap<>();
        this.lockoutCache = new ConcurrentHashMap<>();
    }

    public void loginSucceeded(String key) {
        attemptsCache.remove(key);
        lockoutCache.remove(key);
    }

    public void loginFailed(String key) {
        int attempts = attemptsCache.getOrDefault(key, 0);
        attempts++;
        attemptsCache.put(key, attempts);
        if (attempts >= MAX_ATTEMPT) {
            lockoutCache.put(key, System.currentTimeMillis());
        }
    }

    public boolean isBlocked(String key) {
        if (lockoutCache.containsKey(key)) {
            long lockTime = lockoutCache.get(key);
            if (System.currentTimeMillis() - lockTime < LOCK_TIME_MS) {
                return true;
            } else {
                lockoutCache.remove(key);
                attemptsCache.remove(key);
                return false;
            }
        }
        return false;
    }
}

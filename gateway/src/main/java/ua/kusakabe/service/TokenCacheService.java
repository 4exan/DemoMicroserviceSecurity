package ua.kusakabe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenCacheService {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public TokenCacheService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheToken(String token) {
        redisTemplate.opsForValue().set(token, "valid", 30, TimeUnit.MINUTES);
    }

    public boolean isTokenCached(String token) {
        return redisTemplate.hasKey(token);
    }

    public void removeCachedToken(String token) {
        redisTemplate.delete(token);
    }

}

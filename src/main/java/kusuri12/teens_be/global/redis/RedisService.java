package kusuri12.teens_be.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    public static final String BLACKLIST_PREFIX = "BlackList:";

    private final RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value, Long expireTime) {
        redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /* 블랙리스트 로직 */

    public boolean isBlackList(String accessToken) {
        return get(BLACKLIST_PREFIX + accessToken) != null;
    }

    public void addToBlackList(String accessToken, String username, long expiration) {
        String blackListKey = BLACKLIST_PREFIX + accessToken;
        set(blackListKey, username, expiration);
    }
}

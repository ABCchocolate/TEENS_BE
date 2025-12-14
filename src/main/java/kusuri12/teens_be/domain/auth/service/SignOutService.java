package kusuri12.teens_be.domain.auth.service;

import kusuri12.teens_be.domain.user.domain.repository.UserRepository;
import kusuri12.teens_be.global.jwt.JwtTokenProvider;
import kusuri12.teens_be.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignOutService {

    private final UserRepository userRepository;
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;

    public void signOut(String accessToken, String username) {
        redisService.delete("RT:" + username);

        long expiration = jwtTokenProvider.getExpiration(accessToken);

        if (expiration > 0) {
            String blackListKey = "BlackList:" + accessToken;
            redisService.set(blackListKey, username, expiration);
        }
    }

    public void quit(String accessToken, String username) {
        signOut(accessToken, username);
        userRepository.deleteByUsername(username);
    }

    public boolean isBlackList(String accessToken) {
        return redisService.get("BlackList:" + accessToken) != null;
    }
}

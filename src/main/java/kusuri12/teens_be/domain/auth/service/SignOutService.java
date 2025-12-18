package kusuri12.teens_be.domain.auth.service;

import kusuri12.teens_be.domain.user.domain.repository.UserRepository;
import kusuri12.teens_be.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignOutService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public void signOut(String accessToken, String username) {
        jwtTokenProvider.deleteRefreshToken(username);

        long expiration = jwtTokenProvider.getExpiration(accessToken);

        if (expiration > 0) {
            jwtTokenProvider.addToBlackList(accessToken, username, expiration);
        }
    }

    public void quit(String accessToken, String username) {
        signOut(accessToken, username);
        userRepository.deleteByUsername(username);
    }
}

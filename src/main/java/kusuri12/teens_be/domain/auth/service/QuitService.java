package kusuri12.teens_be.domain.auth.service;

import kusuri12.teens_be.domain.user.repository.UserRepository;
import kusuri12.teens_be.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuitService {

    private final SignOutService signOutService;
    private final UserRepository userRepository;

    @Transactional
    public void execute(String accessToken) {
        String username = signOutService.execute(accessToken);
        userRepository.deleteByUsername(username);
    }
}

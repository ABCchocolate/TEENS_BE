package kusuri12.teens_be.domain.auth.service;

import kusuri12.teens_be.domain.auth.exception.AuthErrorCode;
import kusuri12.teens_be.domain.auth.presentation.dto.request.SignInRequest;
import kusuri12.teens_be.domain.auth.presentation.dto.request.SignUpRequest;
import kusuri12.teens_be.domain.auth.presentation.dto.response.SignInResponse;
import kusuri12.teens_be.domain.user.domain.User;
import kusuri12.teens_be.domain.user.repository.UserRepository;
import kusuri12.teens_be.domain.user.domain.Role;
import kusuri12.teens_be.global.error.exception.TeensException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final SignInService signInService;

    @Transactional
    public SignInResponse execute(SignUpRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new TeensException(AuthErrorCode.USERNAME_ALREADY_EXISTS);
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new TeensException(AuthErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(encoder.encode(request.password()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        return signInService.execute(SignInRequest.of(request.username(), request.password()));
    }
}

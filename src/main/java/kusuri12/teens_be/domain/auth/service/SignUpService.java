package kusuri12.teens_be.domain.auth.service;

import kusuri12.teens_be.domain.auth.presentation.dto.request.SignUpRequest;
import kusuri12.teens_be.domain.user.domain.User;
import kusuri12.teens_be.domain.user.repository.UserRepository;
import kusuri12.teens_be.domain.user.domain.Role;
import kusuri12.teens_be.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    @Transactional
    public void signUp(SignUpRequest request) {

        if (!request.password().equals(request.confirmPassword())) {
            throw PasswordConfirmWrongException.EXCEPTION;
        }

        if (userRepository.existsByUsername(request.username())) {
            throw UsernameAlreadyExistsException.EXCEPTION;
        }

        if (userRepository.existsByEmail(request.email())) {
            throw EmailAlreadyExistsException.EXCEPTION;
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(encoder.encode(request.password()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }
}

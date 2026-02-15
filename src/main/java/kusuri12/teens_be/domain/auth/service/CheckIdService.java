package kusuri12.teens_be.domain.auth.service;

import kusuri12.teens_be.domain.auth.exception.AuthErrorCode;
import kusuri12.teens_be.domain.auth.presentation.dto.request.CheckIdRequest;
import kusuri12.teens_be.domain.user.repository.UserRepository;
import kusuri12.teens_be.global.error.exception.TeensException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckIdService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public void execute(CheckIdRequest request) {
        if (!userRepository.existsByUsername(request.username())) {
            throw new TeensException(AuthErrorCode.USERNAME_ALREADY_EXISTS);
        }
    }
}
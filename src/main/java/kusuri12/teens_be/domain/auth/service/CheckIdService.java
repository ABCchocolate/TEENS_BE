package kusuri12.teens_be.domain.auth.service;

import kusuri12.teens_be.domain.auth.presentation.dto.request.CheckIdRequest;
import kusuri12.teens_be.domain.auth.presentation.dto.response.CheckIdResponse;
import kusuri12.teens_be.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckIdService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public CheckIdResponse checkId(CheckIdRequest request) {
        return new CheckIdResponse(!userRepository.existsByUsername(request.username()));
    }
}
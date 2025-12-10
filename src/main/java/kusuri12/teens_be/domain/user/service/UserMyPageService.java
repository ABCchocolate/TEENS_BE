package kusuri12.teens_be.domain.user.service;

import kusuri12.teens_be.domain.comment.domain.repository.CommentRepository;
import kusuri12.teens_be.domain.forum.domain.repository.ForumRepository;
import kusuri12.teens_be.domain.user.domain.repository.UserRepository;
import kusuri12.teens_be.domain.user.domain.User;
import kusuri12.teens_be.domain.user.exception.UserNotFoundException;
import kusuri12.teens_be.domain.user.presentation.dto.request.NicknameRequest;
import kusuri12.teens_be.domain.user.presentation.dto.response.UserMeResponse;
import kusuri12.teens_be.global.auth.AuthDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserMyPageService {

    private final UserRepository userRepository;
    private final ForumRepository forumRepository;
    private final CommentRepository commentRepository;
    private final BCryptPasswordEncoder encoder;

    @Transactional(readOnly = true)
    public UserMeResponse getUserMe(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        int forumCount = forumRepository.countByUser_Id(id);
        int commentCount = commentRepository.countByUserId(id);

        return UserMeResponse.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .forumCount(forumCount)
                .commentCount(commentCount)
                .build();
    }

    @Transactional
    public void changeNickname(NicknameRequest request, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        user.updateNickname(request.nickname());
    }

    public void changePassword(String password, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        user.updatePassword(encoder.encode(password));
    }
}
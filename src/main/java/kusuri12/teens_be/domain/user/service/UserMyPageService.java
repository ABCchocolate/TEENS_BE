package kusuri12.teens_be.domain.user.service;

import kusuri12.teens_be.domain.comment.domain.repository.CommentRepository;
import kusuri12.teens_be.domain.forum.domain.repository.ForumRepository;
import kusuri12.teens_be.domain.user.domain.repository.UserRepository;
import kusuri12.teens_be.domain.user.presentation.dto.request.UserDto;
import kusuri12.teens_be.domain.user.domain.User;
import kusuri12.teens_be.global.error.exception.ErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserMyPageService {

    private final UserRepository userRepository;
    private final ForumRepository forumRepository;
    private final CommentRepository commentRepository;

    public UserDto.UserMeResponse getUserMe(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TeensException(ErrorCode.USER_NOT_FOUND));

        Long forumCount = forumRepository.countByUserId(userId);
        Long commentCount = commentRepository.countByUserId(userId);

        return UserDto.UserMeResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .role(user.getRole().name())
                .forumCount(forumCount)
                .commentCount(commentCount)
                .build();
    }
}
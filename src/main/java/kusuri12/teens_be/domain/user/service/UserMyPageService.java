package kusuri12.teens_be.domain.user.service;

import kusuri12.teens_be.domain.comment.domain.repository.CommentRepository;
import kusuri12.teens_be.domain.forum.domain.repository.ForumRepository;
import kusuri12.teens_be.domain.user.domain.repository.UserRepository;
import kusuri12.teens_be.domain.user.domain.User;
import kusuri12.teens_be.domain.user.exception.PasswordMismatchException;
import kusuri12.teens_be.domain.user.exception.SamePasswordException;
import kusuri12.teens_be.domain.user.exception.UserNotFoundException;
import kusuri12.teens_be.domain.user.presentation.dto.request.NicknameRequest;
import kusuri12.teens_be.domain.user.presentation.dto.request.PasswordRequest;
import kusuri12.teens_be.domain.user.presentation.dto.response.UserMeResponse;
import kusuri12.teens_be.global.s3.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserMyPageService {

    private final UserRepository userRepository;
    private final ForumRepository forumRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder encoder;
    private final S3UploadService s3UploadService;

    @Transactional(readOnly = true)
    public UserMeResponse getUserMe(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        int forumCount = forumRepository.countByUser_Id(id);
        int commentCount = commentRepository.countByUserId(id);

        // DB에 저장된 fileKey를 꺼내서 임시 보안 URL 생성
        String profileImage = null;
        if (user.getProfileImg() != null) {
            profileImage = s3UploadService.generatePresignedUrl(user.getProfileImg());
        }

        return UserMeResponse.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .forumCount(forumCount)
                .commentCount(commentCount)
                .profileImg(profileImage)
                .build();
    }

    @Transactional
    public void changeNickname(NicknameRequest request, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        user.updateNickname(request.nickname());
    }

    @Transactional
    public void changePassword(PasswordRequest request, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        if (!encoder.matches(request.currentPassword(), user.getPassword())
                || !request.newPassword().equals(request.confirmPassword())) {
            throw PasswordMismatchException.EXCEPTION;
        }

        if (request.currentPassword().equals(request.newPassword())) {
            throw SamePasswordException.EXCEPTION;
        }

        user.updatePassword(encoder.encode(request.newPassword()));
    }

    @Transactional
    public void uploadProfileImg(String profileImg, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        if (user.getProfileImg() != null && !user.getProfileImg().isEmpty()) {
            s3UploadService.delete(user.getProfileImg());
        }

        user.updateProfileImg(profileImg);
    }
}
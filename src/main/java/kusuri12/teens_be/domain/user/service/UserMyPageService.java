package kusuri12.teens_be.domain.user.service;

import kusuri12.teens_be.domain.user.exception.UserErrorCode;
import kusuri12.teens_be.domain.user.repository.UserRepository;
import kusuri12.teens_be.domain.user.domain.User;
import kusuri12.teens_be.domain.user.presentation.dto.request.NicknameRequest;
import kusuri12.teens_be.domain.user.presentation.dto.request.PasswordRequest;
import kusuri12.teens_be.domain.user.presentation.dto.response.UserMeResponse;
import kusuri12.teens_be.global.aws.s3.S3UploadService;
import kusuri12.teens_be.global.error.exception.TeensException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserMyPageService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final S3UploadService s3UploadService;

    @Transactional(readOnly = true)
    public UserMeResponse getUserMe(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new TeensException(UserErrorCode.USER_NOT_FOUND));

        // 프로필 이미지
        String profileImage = (user.getProfileImg() != null) ?
                s3UploadService.getFileUrl(user.getProfileImg()) : null;

        return UserMeResponse.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .forumCount(user.getForumCount())
                .commentCount(user.getCommentCount())
                .profileImg(profileImage)
                .build();
    }

    @Transactional
    public void changeNickname(NicknameRequest request, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new TeensException(UserErrorCode.USER_NOT_FOUND));

        user.updateNickname(request.nickname());
    }

    @Transactional
    public void changePassword(PasswordRequest request, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new TeensException(UserErrorCode.USER_NOT_FOUND));

        if (!encoder.matches(request.currentPassword(), user.getPassword())) {
            throw new TeensException(UserErrorCode.PASSWORD_CONFIRM_WRONG);
        }

        user.updatePassword(encoder.encode(request.newPassword()));
    }

    @Transactional
    public void uploadProfileImg(String profileImg, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new TeensException(UserErrorCode.USER_NOT_FOUND));

        if (user.getProfileImg() != null && !user.getProfileImg().isEmpty()) {
            s3UploadService.delete(user.getProfileImg());
        }

        user.updateProfileImg(profileImg);
    }
}
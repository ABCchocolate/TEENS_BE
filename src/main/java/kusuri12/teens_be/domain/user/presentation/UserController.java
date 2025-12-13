package kusuri12.teens_be.domain.user.presentation;

import kusuri12.teens_be.domain.user.presentation.dto.request.NicknameRequest;
import kusuri12.teens_be.domain.user.presentation.dto.request.PasswordRequest;
import kusuri12.teens_be.domain.user.presentation.dto.response.UserMeResponse;
import kusuri12.teens_be.domain.user.service.UserMyPageService;
import kusuri12.teens_be.global.auth.AuthDetails;
import kusuri12.teens_be.global.s3.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user/me")
@RequiredArgsConstructor
public class UserController {

    private final UserMyPageService userMypageService;
    private final S3UploadService s3UploadService;

    @GetMapping
    public ResponseEntity<UserMeResponse> getUserMe(
            @AuthenticationPrincipal AuthDetails authDetails
    ) {
        Long id = authDetails.getId();
        UserMeResponse response = userMypageService.getUserMe(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/nickname")
    public void changeNickname(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody NicknameRequest request) {
        Long id = authDetails.getId();
        userMypageService.changeNickname(request, id);
    }

    @PutMapping("/password")
    public void changePassword(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody PasswordRequest request) {
        Long id = authDetails.getId();
        userMypageService.changePassword(request, id);
    }

    @PutMapping("/profile-image")
    public void uploadProfileImg(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestPart MultipartFile file) {
        Long id = authDetails.getId();
        String imgUrl = s3UploadService.upload(file, "user/profiles/");

        userMypageService.uploadProfileImg(imgUrl, id);
    }
}
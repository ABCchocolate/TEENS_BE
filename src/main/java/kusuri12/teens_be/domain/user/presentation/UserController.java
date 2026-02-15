package kusuri12.teens_be.domain.user.presentation;

import jakarta.validation.Valid;
import kusuri12.teens_be.domain.user.presentation.dto.request.NicknameRequest;
import kusuri12.teens_be.domain.user.presentation.dto.request.PasswordRequest;
import kusuri12.teens_be.domain.user.presentation.dto.response.UserMeResponse;
import kusuri12.teens_be.domain.user.service.PasswordValidator;
import kusuri12.teens_be.domain.user.service.UserMyPageService;
import kusuri12.teens_be.global.auth.AuthDetails;
import kusuri12.teens_be.global.aws.s3.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user/me")
@RequiredArgsConstructor
public class UserController {

    private final UserMyPageService userMypageService;
    private final S3UploadService s3UploadService;

    private final PasswordValidator passwordValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(passwordValidator);
    }

    @GetMapping
    public ResponseEntity<UserMeResponse> getUserMe(
            @AuthenticationPrincipal AuthDetails authDetails
    ) {
        Long id = authDetails.getId();
        UserMeResponse response = userMypageService.getUserMe(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/nickname")
    public ResponseEntity<Void> changeNickname(
            @AuthenticationPrincipal AuthDetails authDetails,
            @Valid @RequestBody NicknameRequest request) {
        Long id = authDetails.getId();
        userMypageService.changeNickname(request, id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal AuthDetails authDetails,
            @Valid @RequestBody PasswordRequest request) {
        Long id = authDetails.getId();
        userMypageService.changePassword(request, id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/profile-image")
    public ResponseEntity<Void> uploadProfileImg(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestPart MultipartFile image) {
        Long id = authDetails.getId();
        String imgKey = s3UploadService.upload(image, "user/profiles/");

        userMypageService.uploadProfileImg(imgKey, id);
        return ResponseEntity.noContent().build();
    }
}
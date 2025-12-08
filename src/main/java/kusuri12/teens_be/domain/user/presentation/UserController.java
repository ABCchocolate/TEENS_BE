package kusuri12.teens_be.domain.user.presentation;

import kusuri12.teens_be.domain.user.presentation.dto.request.UserDto;
import kusuri12.teens_be.domain.user.service.UserMyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserMyPageService userMypageService;

    @GetMapping("/me")
    public ResponseEntity<UserDto.UserMeResponse> getUserMe(@RequestParam Long userId) {
        return ResponseEntity.ok(userMypageService.getUserMe(userId));
    }
}
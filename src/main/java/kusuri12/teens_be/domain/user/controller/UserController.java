package kusuri12.teens_be.domain.user.controller;

import kusuri12.teens_be.domain.user.dto.UserDto;
import kusuri12.teens_be.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto.UserMeResponse> getUserMe(@RequestParam Long userId) {
        return ResponseEntity.ok(userService.getUserMe(userId));
    }
}
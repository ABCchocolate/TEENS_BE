package kusuri12.teens_be.domain.auth.presentation;

import jakarta.validation.Valid;
import kusuri12.teens_be.domain.auth.presentation.dto.request.CheckIdRequest;
import kusuri12.teens_be.domain.auth.presentation.dto.request.ReissueRequest;
import kusuri12.teens_be.domain.auth.presentation.dto.request.SignInRequest;
import kusuri12.teens_be.domain.auth.presentation.dto.request.SignUpRequest;
import kusuri12.teens_be.domain.auth.presentation.dto.response.CheckIdResponse;
import kusuri12.teens_be.domain.auth.presentation.dto.response.SignInResponse;
import kusuri12.teens_be.domain.auth.service.*;
import kusuri12.teens_be.global.auth.AuthDetails;
import kusuri12.teens_be.global.jwt.JwtTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SignUpService signUpService;
    private final SignInService signInService;
    private final CheckIdService checkIdService;
    private final SignOutService signOutService;
    private final ReissueService reissueService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest request) {
        signUpService.signUp(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(@Valid @RequestBody SignInRequest request) {
        return ResponseEntity.ok(signInService.signIn(request));
    }

    @GetMapping("/check-id")
    public ResponseEntity<CheckIdResponse> checkId(@ModelAttribute CheckIdRequest request) {
        return ResponseEntity.ok(checkIdService.checkId(request));
    }

    @PostMapping("/sign-out")
    public ResponseEntity<Void> signOut(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestHeader("Authorization") String accessTokenHeader) {
        String accessToken = accessTokenHeader.substring(7); // "Bearer " 제거
        signOutService.signOut(accessToken, authDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/quit")
    public ResponseEntity<Void> quit(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestHeader("Authorization") String accessTokenHeader) {
        String accessToken = accessTokenHeader.substring(7);
        signOutService.quit(accessToken, authDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<JwtTokens> reissue(
            @RequestBody ReissueRequest request) {
        return ResponseEntity.ok(reissueService.reissue(request));
    }
}
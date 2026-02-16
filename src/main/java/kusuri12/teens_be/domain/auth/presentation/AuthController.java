package kusuri12.teens_be.domain.auth.presentation;

import jakarta.validation.Valid;
import kusuri12.teens_be.domain.auth.presentation.dto.request.CheckIdRequest;
import kusuri12.teens_be.domain.auth.presentation.dto.request.ReissueRequest;
import kusuri12.teens_be.domain.auth.presentation.dto.request.SignInRequest;
import kusuri12.teens_be.domain.auth.presentation.dto.request.SignUpRequest;
import kusuri12.teens_be.domain.auth.presentation.dto.response.SignInResponse;
import kusuri12.teens_be.domain.auth.service.*;
import kusuri12.teens_be.domain.auth.service.validator.SignUpValidator;
import kusuri12.teens_be.global.auth.AuthDetails;
import kusuri12.teens_be.global.jwt.JwtTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SignUpService signUpService;
    private final SignInService signInService;
    private final CheckIdService checkIdService;
    private final SignOutService signOutService;
    private final QuitService quitService;
    private final ReissueService reissueService;

    private final SignUpValidator signUpValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(signUpValidator);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<SignInResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        return ResponseEntity.ok(signUpService.execute(request));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(@Valid @RequestBody SignInRequest request) {
        return ResponseEntity.ok(signInService.execute(request));
    }

    @GetMapping("/check-id")
    public ResponseEntity<Void> checkId(@ModelAttribute CheckIdRequest request) {
        checkIdService.execute(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/sign-out")
    public ResponseEntity<Void> signOut(
            @RequestHeader("Authorization") String accessTokenHeader) {
        String accessToken = accessTokenHeader.substring(7); // "Bearer " 제거
        signOutService.execute(accessToken);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/quit")
    public ResponseEntity<Void> quit(
            @RequestHeader("Authorization") String accessTokenHeader) {
        String accessToken = accessTokenHeader.substring(7);
        quitService.execute(accessToken);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<JwtTokens> reissue(
            @RequestHeader("Authorization") String accessTokenHeader,
            @RequestBody ReissueRequest request) {
        String accessToken = accessTokenHeader.substring(7);
        return ResponseEntity.ok(reissueService.execute(accessToken, request));
    }
}
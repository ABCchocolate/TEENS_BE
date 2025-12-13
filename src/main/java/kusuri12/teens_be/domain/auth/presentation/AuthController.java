package kusuri12.teens_be.domain.auth.presentation;

import kusuri12.teens_be.domain.auth.presentation.dto.request.SignInRequest;
import kusuri12.teens_be.domain.auth.presentation.dto.request.SignUpRequest;
import kusuri12.teens_be.domain.auth.presentation.dto.response.SignInResponse;
import kusuri12.teens_be.domain.auth.service.SignInService;
import kusuri12.teens_be.domain.auth.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SignUpService signUpService;
    private final SignInService signInService;

    @PostMapping("/sign-up")
    public void signUp(SignUpRequest request) {
        signUpService.signUp(request);
    }

    @PostMapping("/sign-in")
    public SignInResponse signIn(SignInRequest request) {
        return signInService.signIn(request);
    }

}

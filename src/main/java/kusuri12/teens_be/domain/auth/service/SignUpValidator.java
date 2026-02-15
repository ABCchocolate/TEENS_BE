package kusuri12.teens_be.domain.auth.service;

import kusuri12.teens_be.domain.auth.exception.AuthErrorCode;
import kusuri12.teens_be.domain.auth.presentation.dto.request.SignUpRequest;
import kusuri12.teens_be.global.validation.validator.AbstractValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class SignUpValidator extends AbstractValidator<SignUpRequest> {

    private final PasswordEncoder encoder;

    @Override
    protected Class<SignUpRequest> getTargetClass() {
        return SignUpRequest.class;
    }

    @Override
    protected void doValidate(SignUpRequest dto, Errors errors) {

        if (!dto.password().equals(dto.confirmPassword())) {
            errors.rejectValue(
                    "confirmPassword",
                    AuthErrorCode.PASSWORD_CONFIRM_WRONG.getCode(),
                    AuthErrorCode.PASSWORD_CONFIRM_WRONG.getMessage());
        }
    }
}

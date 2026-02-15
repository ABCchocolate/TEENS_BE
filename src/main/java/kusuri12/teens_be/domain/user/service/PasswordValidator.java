package kusuri12.teens_be.domain.user.service;

import kusuri12.teens_be.domain.user.exception.UserErrorCode;
import kusuri12.teens_be.domain.user.presentation.dto.request.PasswordRequest;
import kusuri12.teens_be.global.validation.validator.AbstractValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class PasswordValidator extends AbstractValidator<PasswordRequest> {

    private final PasswordEncoder encoder;

    @Override
    protected Class<PasswordRequest> getTargetClass() {
        return PasswordRequest.class;
    }

    @Override
    protected void doValidate(PasswordRequest dto, Errors errors) {

        if (encoder.matches(dto.currentPassword(), dto.newPassword())) {
            errors.rejectValue(
                    "newPassword",
                    UserErrorCode.PASSWORD_CONFIRM_WRONG.getCode(),
                    UserErrorCode.PASSWORD_CONFIRM_WRONG.getMessage());
        }

        if (!dto.newPassword().equals(dto.confirmPassword())) {
            errors.rejectValue(
                    "confirmPassword",
                    UserErrorCode.SAME_PASSWORD.getCode(),
                    UserErrorCode.SAME_PASSWORD.getMessage());
        }
    }

}

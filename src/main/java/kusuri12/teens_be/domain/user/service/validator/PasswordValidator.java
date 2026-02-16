package kusuri12.teens_be.domain.user.service.validator;

import kusuri12.teens_be.domain.user.exception.UserErrorCode;
import kusuri12.teens_be.domain.user.presentation.dto.request.PasswordRequest;
import kusuri12.teens_be.global.validation.validator.AbstractValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class PasswordValidator extends AbstractValidator<PasswordRequest> {

    @Override
    protected Class<PasswordRequest> getTargetClass() {
        return PasswordRequest.class;
    }

    @Override
    protected void doValidate(PasswordRequest dto, Errors errors) {

        // 현재 패스워드랑 새로운 패스워드가 같을 때
        if (dto.currentPassword().equals(dto.newPassword())) {
            errors.rejectValue(
                    "newPassword",
                    UserErrorCode.SAME_PASSWORD.getCode(),
                    UserErrorCode.SAME_PASSWORD.getMessage());
        }

        // confirm 패스워드 불일치
        if (!dto.newPassword().equals(dto.confirmPassword())) {
            errors.rejectValue(
                    "confirmPassword",
                    UserErrorCode.PASSWORD_CONFIRM_WRONG.getCode(),
                    UserErrorCode.PASSWORD_CONFIRM_WRONG.getMessage());
        }
    }
}

package kusuri12.teens_be.domain.user.exception;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;

public class PasswordMismatchException extends TeensException {
    public static final PasswordMismatchException EXCEPTION = new PasswordMismatchException();

    PasswordMismatchException() {
        super(ErrorCode.PASSWORD_CONFIRM_WRONG);
    }
}

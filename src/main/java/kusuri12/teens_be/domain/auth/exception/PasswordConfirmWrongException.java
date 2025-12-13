package kusuri12.teens_be.domain.auth.exception;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;

public class PasswordConfirmWrongException extends TeensException {
    public final static TeensException EXCEPTION = new PasswordConfirmWrongException();
    private PasswordConfirmWrongException() {
        super(ErrorCode.PASSWORD_CONFIRM_WRONG);
    }
}

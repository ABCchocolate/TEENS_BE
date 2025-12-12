package kusuri12.teens_be.domain.auth.exception;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;

public class EmailAlreadyExistsException extends TeensException {
    public static final EmailAlreadyExistsException EXCEPTION = new EmailAlreadyExistsException();
    private EmailAlreadyExistsException() {
        super(ErrorCode.EMAIL_ALREADY_EXISTS);
    }
}

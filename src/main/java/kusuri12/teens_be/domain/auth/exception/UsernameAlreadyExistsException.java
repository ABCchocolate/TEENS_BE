package kusuri12.teens_be.domain.auth.exception;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;

public class UsernameAlreadyExistsException extends TeensException {
    public static final UsernameAlreadyExistsException EXCEPTION = new UsernameAlreadyExistsException();
    private UsernameAlreadyExistsException() {
        super(ErrorCode.USERNAME_ALREADY_EXISTS);
    }
}

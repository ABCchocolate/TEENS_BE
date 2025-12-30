package kusuri12.teens_be.global.jwt.exception;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;

public class InvalidTokenException extends TeensException {
    public static final InvalidTokenException EXCEPTION = new InvalidTokenException();
    private InvalidTokenException() {
        super(ErrorCode.INVALID_JWT);
    }
}

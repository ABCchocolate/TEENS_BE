package kusuri12.teens_be.global.jwt.exception;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;

public class InvalidJwtException extends TeensException {
    public static final InvalidJwtException EXCEPTION = new InvalidJwtException();
    private InvalidJwtException() {
        super(ErrorCode.INVALID_JWT);
    }
}

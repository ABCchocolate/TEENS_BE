package kusuri12.teens_be.global.jwt.exception;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;

public class ExpiredTokenException extends TeensException {
    public static final ExpiredTokenException EXCEPTION = new ExpiredTokenException();
    private ExpiredTokenException() {
        super(ErrorCode.EXPIRED_JWT);
    }
}

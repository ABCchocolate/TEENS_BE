package kusuri12.teens_be.global.jwt.exception;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;

public class ExpiredJwtException extends TeensException {

    public static final ExpiredJwtException EXCEPTION = new ExpiredJwtException();

    public ExpiredJwtException() {
        super(ErrorCode.EXPIRED_JWT);
    }
}

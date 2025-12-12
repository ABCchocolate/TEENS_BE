package kusuri12.teens_be.domain.user.exception;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;

public class SamePasswordException extends TeensException {
    public static final SamePasswordException EXCEPTION = new SamePasswordException();

    SamePasswordException() {
        super(ErrorCode.SAME_PASSWORD_WRONG);
    }
}

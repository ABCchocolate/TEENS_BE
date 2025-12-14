package kusuri12.teens_be.global.jwt.exception;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;

public class TokenNotFoundException extends TeensException {
    public static final TokenNotFoundException EXCEPTION = new TokenNotFoundException();
    private TokenNotFoundException() {
        super(ErrorCode.TOKEN_NOT_FOUND);
    }
}

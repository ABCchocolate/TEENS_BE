package kusuri12.teens_be.domain.user.exception;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;

public class UserNotFoundException extends TeensException {

    public static final TeensException EXCEPTION = new UserNotFoundException();

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}

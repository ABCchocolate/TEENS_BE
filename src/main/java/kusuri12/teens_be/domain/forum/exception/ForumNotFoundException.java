package kusuri12.teens_be.domain.forum.exception;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;

public class ForumNotFoundException extends TeensException {

    public static final TeensException EXCEPTION = new ForumNotFoundException();

    private ForumNotFoundException() {
        super(ErrorCode.FORUM_NOT_FOUND);
    }
}
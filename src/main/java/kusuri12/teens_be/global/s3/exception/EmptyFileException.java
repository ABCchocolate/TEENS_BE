package kusuri12.teens_be.global.s3.exception;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;

public class EmptyFileException extends TeensException {
    public static final EmptyFileException EXCEPTION = new EmptyFileException();

    private EmptyFileException() {
        super(ErrorCode.FILE_IS_EMPTY);
    }
}

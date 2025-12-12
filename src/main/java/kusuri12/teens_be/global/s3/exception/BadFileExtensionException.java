package kusuri12.teens_be.global.s3.exception;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;

public class BadFileExtensionException extends TeensException {
    public static final BadFileExtensionException EXCEPTION = new BadFileExtensionException();
    private BadFileExtensionException() {
        super(ErrorCode.BAD_FILE_EXTENSION);
    }
}

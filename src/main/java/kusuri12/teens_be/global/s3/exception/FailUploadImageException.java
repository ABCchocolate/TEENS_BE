package kusuri12.teens_be.global.s3.exception;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FailUploadImageException extends TeensException {
    public FailUploadImageException(Exception e) {
        super(ErrorCode.FAIL_UPLOAD_IMAGE);
        log.error(e.getMessage());
    }
}
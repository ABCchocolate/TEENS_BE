package kusuri12.teens_be.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class TeensException extends RuntimeException {

    private final ErrorCode errorCode;

    public TeensException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public TeensException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }
}
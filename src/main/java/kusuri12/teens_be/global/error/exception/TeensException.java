package kusuri12.teens_be.global.error.exception;

import lombok.Getter;

@Getter
public abstract class TeensException extends RuntimeException {
    private final ErrorCode errorCode;
    
    protected TeensException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
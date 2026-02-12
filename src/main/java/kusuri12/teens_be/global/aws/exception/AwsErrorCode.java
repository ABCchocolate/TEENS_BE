package kusuri12.teens_be.global.s3.exception;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum S3ErrorCode implements ErrorCode {

    FILE_IS_EMPTY(HttpStatus.NOT_FOUND, "파일이 존재하지 않습니다"),
    

    private HttpStatus status;
    private String code;
    private String message;

    @Override
    public HttpStatus getStatus() {
        return null;
    }

    @Override
    public String getCode() {
        return "";
    }

    @Override
    public String getMessage() {
        return "";
    }
}

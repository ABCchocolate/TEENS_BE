package kusuri12.teens_be.global.aws.exception;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum AwsErrorCode implements ErrorCode {

    // 400
    BAD_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "AWS_400_01", "잘못된 파일 확장자입니다"),
    MAX_UPLOAD_FILE(HttpStatus.BAD_REQUEST, "AWS_400_02", "최대 업로드 파일 크기를 초과했습니다"),

    // 404
    FILE_IS_EMPTY(HttpStatus.NOT_FOUND, "AWS_404", "파일이 존재하지 않습니다"),

    // 500
    FAIL_HANDLE_IMAGE(HttpStatus.INTERNAL_SERVER_ERROR, "AWS_500", "파일 처리 중 오류가 발생했습니다");

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

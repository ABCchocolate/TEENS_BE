package kusuri12.teens_be.domain.user.exception;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {

    // 400
    PASSWORD_CONFIRM_WRONG(HttpStatus.BAD_REQUEST, "USR_400_01", "비밀번호가 일치하지 않습니다."),
    SAME_PASSWORD(HttpStatus.BAD_REQUEST, "USR_400_02", "새 비밀번호가 현재 비밀번호와 같습니다."),

    // 404
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USR_404", "해당 사용자를 찾을 수 없습니다.");

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

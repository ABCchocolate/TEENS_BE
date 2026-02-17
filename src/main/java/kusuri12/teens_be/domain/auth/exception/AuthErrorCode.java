package kusuri12.teens_be.domain.auth.exception;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    // 400
    PASSWORD_CONFIRM_WRONG(HttpStatus.BAD_REQUEST, "AUT_400_01", "비밀번호가 일치하지 않습니다."),
    EMAIL_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "AUT_400_02", "인증 시간이 만료되었습니다. 다시 인증 코드를 발급받아주세요."),
    EMAIL_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "AUT_400_03", "인증 코드가 일치하지 않습니다. 다시 확인해주세요."),
    INVALID_TOKEN_PAIR(HttpStatus.BAD_REQUEST, "AUTH_400_04", "Access Token과 Refresh Token의 정보가 일치하지 않습니다."),

    // 401
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "AUT_401", "아이디 또는 비밀번호가 일치하지 않습니다."),

    // 403
    TOKEN_THEFT_DETECTED(HttpStatus.FORBIDDEN, "AUTH_403", "토큰 탈취가 의심됩니다. 모든 세션을 종료합니다."),

    // 404
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "AUT_404", "해당 Refresh Token을 찾을 수 없습니다."),

    // 409
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "AUT_409_01", "이미 사용 중인 이메일입니다."),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "AUT_409_02", "이미 사용 중인 아이디입니다.");

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

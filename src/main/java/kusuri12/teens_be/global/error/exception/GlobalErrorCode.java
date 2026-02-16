package kusuri12.teens_be.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum GlobalErrorCode implements ErrorCode {

    // 400
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "GLB_400_01", "요청 데이터 유효성 검증에 실패했습니다."),
    REQUEST_NOT_READABLE(HttpStatus.BAD_REQUEST, "GLB_400_02", "요청 본문의 형식이 잘못되었습니다."),

    // 401
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "GLB_401_01", "로그인 후 이용할 수 있습니다."),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "GLB_401_02", "유효하지 않은 토큰입니다."),
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "GLB_401_03", "만료된 토큰입니다."),

    // 403
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "GLB_403", "접근 권한이 없습니다."),

    // 404
    NOT_FOUND(HttpStatus.NOT_FOUND, "GLB_404", "해당 리소스를 찾을 수 없습니다."),

    // 서버 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GLB_500", "내부 서버 오류");

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getCode() {
        return "";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
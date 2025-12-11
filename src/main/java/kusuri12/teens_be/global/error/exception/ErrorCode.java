package kusuri12.teens_be.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // access Token 만료됨
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    // 유저 정보를 찾을 수 없음
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),

    // 유효성 검증 실패
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "요청 데이터 유효성 검증에 실패했습니다."),

    // username 관련
    INVALID_USERNAME_LENGTH(HttpStatus.BAD_REQUEST, "아이디는 4자 이상 20자 이하로 입력해야 합니다."),
    USERNAME_PATTERN_VIOLATION(HttpStatus.BAD_REQUEST, "유효하지 않은 아이디입니다."),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다."),

    // email 관련
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일 형식입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    AUTH_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "인증 코드가 일치하지 않습니다. 다시 확인해주세요."),
    AUTH_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "인증 시간이 만료되었습니다. 다시 인증 코드를 발급받아주세요."),

    // 비밀번호 관련
    PASSWORD_PATTERN_VIOLATION(HttpStatus.BAD_REQUEST, "비밀번호 규칙을 위반했습니다."),
    PASSWORD_CONFIRM_WRONG(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다."),

    // 서버 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),

    // 정보 게시판 관련
    INFO_ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),

    // 티키타카 게시판 관련
    FORUM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
package kusuri12.teens_be.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "요청 데이터 유효성 검증에 실패했습니다."),
    INVALID_USERNAME_LENGTH(HttpStatus.BAD_REQUEST, "아이디는 4자 이상 20자 이하로 입력해야 합니다."),
    USERNAME_PATTERN_VIOLATION(HttpStatus.BAD_REQUEST, "유효하지 않은 아이디입니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일 형식입니다."),
    AUTH_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "인증 시간이 만료되었습니다. 다시 인증 코드를 발급받아주세요."),
    AUTH_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "인증 코드가 일치하지 않습니다. 다시 확인해주세요."),
    PASSWORD_PATTERN_VIOLATION(HttpStatus.BAD_REQUEST, "비밀번호 규칙을 위반했습니다."),
    PASSWORD_CONFIRM_WRONG(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    NO_TITLE(HttpStatus.BAD_REQUEST, "제목을 입력해주세요."),
    OVER_CONTENT(HttpStatus.BAD_REQUEST, "내용은 2000자를 넘을 수 없습니다."),
    NO_COMMENT_CONTENT(HttpStatus.BAD_REQUEST, "내용을 작성해주세요."),
    FILE_IS_EMPTY(HttpStatus.BAD_REQUEST, "파일이 존재하지 않습니다"),
    BAD_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "잘못된 파일 확장자입니다"),
    MAX_UPLOAD_FILE(HttpStatus.BAD_REQUEST, "최대 업로드 파일 크기를 초과했습니다"),

    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다."),

    NO_AUTHOR(HttpStatus.FORBIDDEN, "작성자만 수정할 수 있습니다."),
    NO_ADMIN(HttpStatus.FORBIDDEN, "정보 게시판은 관리자만 관리할 수 있습니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
    FORUM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다."),
    INFO_ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),

    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다."),

    // 서버 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류"),
    FAIL_UPLOAD_IMAGE(HttpStatus.INTERNAL_SERVER_ERROR, "파일 처리 중 오류가 발생했습니다");

    private final HttpStatus status;
    private final String message;
}
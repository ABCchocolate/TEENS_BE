package kusuri12.teens_be.domain.information.exception;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum InfoErrorCode implements ErrorCode {

    // 403
    NO_AUTHOR(HttpStatus.FORBIDDEN, "INF_403_01", "작성자만 수정할 수 있습니다."),
    NO_ADMIN(HttpStatus.FORBIDDEN, "INF_403_02","정보 게시판은 관리자만 관리할 수 있습니다."),

    // 404
    INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "INF_404","해당 게시글을 찾을 수 없습니다.");

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

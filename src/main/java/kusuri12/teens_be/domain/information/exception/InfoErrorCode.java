package kusuri12.teens_be.domain.information.exception;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum InfoErrorCode implements ErrorCode {

    // 400
    TITLE_EMPTY(HttpStatus.BAD_REQUEST, "INF_400_01", "제목은 비어있을 수 없습니다."),
    CONTENT_EMPTY(HttpStatus.BAD_REQUEST, "INF_400_02", "내용은 비어있을 수 없습니다."),

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

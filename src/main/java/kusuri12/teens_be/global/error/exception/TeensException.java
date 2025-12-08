package kusuri12.teens_be.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TeensException extends RuntimeException {

    private final ErrorCode errorCode;
}

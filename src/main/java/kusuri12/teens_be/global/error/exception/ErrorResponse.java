package kusuri12.teens_be.global.error.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ErrorResponse(HttpStatus httpStatus, String message) {
}

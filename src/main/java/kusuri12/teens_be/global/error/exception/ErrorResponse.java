package kusuri12.teens_be.global.error.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Builder
public record ErrorResponse(
        HttpStatus status,
        String code,
        String message,
        Map<String, String> errors
) { }

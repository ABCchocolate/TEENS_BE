package kusuri12.teens_be.global.error.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User Not Found"),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "Username Already Exists"),
    PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST, "Password Incorrect"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");

    private final HttpStatus status;
    private final String message;
}

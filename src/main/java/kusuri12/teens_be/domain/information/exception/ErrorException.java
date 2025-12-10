package kusuri12.teens_be.domain.information.exception;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;

public class ErrorException extends TeensException {
  public ErrorException(ErrorCode errorCode) {
    super(errorCode);
  }
}
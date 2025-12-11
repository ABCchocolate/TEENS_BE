package kusuri12.teens_be.domain.information.exception;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;

public class informayion extends TeensException {
  public informayion(ErrorCode errorCode) {
    super(errorCode);
  }
}
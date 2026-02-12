package kusuri12.teens_be.domain.information.exception;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;

public class InformationNotFoundException extends TeensException {
    public static final TeensException EXCEPTION = new InformationNotFoundException();

    private InformationNotFoundException() {
      super(ErrorCode.INFO_ARTICLE_NOT_FOUND);
    }
}
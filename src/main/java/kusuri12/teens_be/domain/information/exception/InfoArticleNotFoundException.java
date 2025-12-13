package kusuri12.teens_be.domain.information.exception;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;

public class InfoArticleNotFoundException extends TeensException {
    public static final TeensException EXCEPTION = new InfoArticleNotFoundException();

    private InfoArticleNotFoundException() {
      super(ErrorCode.INFO_ARTICLE_NOT_FOUND);
  }
}
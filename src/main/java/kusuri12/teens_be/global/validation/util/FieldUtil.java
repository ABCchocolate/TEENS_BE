package kusuri12.teens_be.global.validation.util;

import kusuri12.teens_be.global.error.exception.ErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;
import org.springframework.lang.Contract;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

public class FieldUtil {

    private FieldUtil() {}

    // 메서드 체이닝 <T> T
    @Contract("null, _ -> fail")
    public static <T> T notNull(@Nullable T object, ErrorCode errorCode) {
        if (object == null) {
            throw new TeensException(errorCode);
        }
        return object;
    }

    @Contract("null, _ -> fail")
    public static String hasText(@Nullable String text, ErrorCode errorCode) {
        if (!StringUtils.hasText(text)) {
            throw new TeensException(errorCode);
        }
        return text;
    }
}

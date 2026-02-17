package kusuri12.teens_be.global.security.util;

import kusuri12.teens_be.global.error.exception.GlobalErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;
import kusuri12.teens_be.global.security.userdetails.AuthDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    // 인스턴스화 방지
    private SecurityUtil() {}

    public static Long getCurrentUserId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new TeensException(GlobalErrorCode.UNAUTHORIZED_ACCESS);
        }

        if (authentication.getPrincipal() instanceof AuthDetails authDetails) {
            return authDetails.getId();
        }
        throw new TeensException(GlobalErrorCode.UNAUTHORIZED_ACCESS);
    }

    // ADMIN인지 확인하는 메서드
    public static boolean isAdmin() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return false;
        }

        if (authentication.getPrincipal() instanceof AuthDetails authDetails) {
            return authDetails.getRole().equals("ADMIN");
        }

        return false;
    }
}
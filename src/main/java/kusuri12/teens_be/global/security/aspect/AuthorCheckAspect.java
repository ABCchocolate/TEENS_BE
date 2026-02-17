package kusuri12.teens_be.global.security.aspect;

import kusuri12.teens_be.domain.forum.exception.ForumErrorCode;
import kusuri12.teens_be.global.error.exception.GlobalErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;
import kusuri12.teens_be.global.security.annotation.CheckId;
import kusuri12.teens_be.global.security.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Aspect
@Component
@Slf4j
public class AuthorCheckAspect {

    @Around("@annotation(kusuri12.teens_be.global.security.annotation.CheckAuthor)")
    public Object check(ProceedingJoinPoint joinPoint) throws Throwable {
        Long resourceId = getCheckId(joinPoint);

        // 현재 실행 중인 서비스 객체(Target)를 가져와서 인터페이스로 캐스팅
        Object target = joinPoint.getTarget();

        if (target instanceof Authorizable authorizableService) {

            // 서비스에 정의된 로직을 호출해서 작성자 ID 가져오기
            Long authorId = authorizableService.getAuthorId(resourceId);

            // 현재 유저 정보
            Long currentUserId = SecurityUtil.getCurrentUserId();
            boolean isAdmin = SecurityUtil.isAdmin();

            // 본인 or ADMIN 아닐 때 에러
            if (!authorId.equals(currentUserId) && !isAdmin) {
                throw new TeensException(GlobalErrorCode.NO_AUTHOR);
            }
        } else {
            log.error("🚨 보안 설정 오류: {} 클래스가 Authorizable을 구현하지 않았습니다.", target.getClass().getSimpleName());
            throw new TeensException(GlobalErrorCode.INTERNAL_SERVER_ERROR, "Not Implement Authorizable");
        }

        return joinPoint.proceed();
    }

    private Long getCheckId(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        Annotation[][] parameterAnnotations = signature.getMethod().getParameterAnnotations();

        // @CheckId가 붙은 파라미터 확인
        Long resourceId = null;

        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof CheckId) {
                    if (!(args[i] instanceof Long)) {
                        throw new TeensException(GlobalErrorCode.INVALID_REQUEST);
                    }
                    resourceId = (Long) args[i];
                    break;
                }
            }
        }

        if (resourceId == null) {
            throw new TeensException(GlobalErrorCode.INVALID_REQUEST);
        }

        return resourceId;
    }
}

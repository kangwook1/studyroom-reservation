package com.kangwook.studyroom.global.annotation;

import com.kangwook.studyroom.global.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.kangwook.studyroom.global.common.StatusCode.FORBIDDEN_USER;
import static com.kangwook.studyroom.global.common.StatusCode.INVALID_TOKEN;

@Aspect
@Component
public class AuthAspect {

    @Around("@annotation(authRequired)")
    public Object checkAuth(ProceedingJoinPoint joinPoint, AuthRequired authRequired) throws Throwable {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs.getRequest();
        String token = request.getHeader("Authorization");

        if (token == null || token.isBlank()) {
            throw new CustomException(INVALID_TOKEN);
        }

        // admin-token 체크
        if (authRequired.adminOnly() && !token.equals("admin-token")) {
            throw new CustomException(FORBIDDEN_USER);
        }

        // user-token-<id> 체크 (id 없이 그냥 일반 사용자 인증)
        if (!authRequired.adminOnly() && !token.equals("admin-token") && !token.startsWith("user-token-")) {
            throw new CustomException(INVALID_TOKEN);
        }

        return joinPoint.proceed();
    }
}

package com.kangwook.studyroom.global.annotation;

import com.kangwook.studyroom.global.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.kangwook.studyroom.global.common.StatusCode.*;

@Aspect
@Component
public class AuthAspect {

    @Around("@annotation(authRequired)")
    public Object checkAuth(ProceedingJoinPoint joinPoint, AuthRequired authRequired) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");

        if (token == null || token.isBlank()) {
            throw new CustomException(INVALID_TOKEN);
        }

        if (authRequired.adminOnly()) {
            if (!token.equals("admin-token")) {
                throw new CustomException(FORBIDDEN_USER);
            }
        } else if (authRequired.userOnly()) {
            if (!token.matches("user-token-\\d+")) {
                throw new CustomException(INVALID_TOKEN);
            }
        } else {
            if (!token.equals("admin-token") && !token.matches("user-token-\\d+")) {
                throw new CustomException(INVALID_TOKEN);
            }
        }

        return joinPoint.proceed();
    }
}

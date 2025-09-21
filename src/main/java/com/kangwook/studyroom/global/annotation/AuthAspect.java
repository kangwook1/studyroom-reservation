package com.kangwook.studyroom.global.annotation;

import com.kangwook.studyroom.global.common.Role;
import com.kangwook.studyroom.global.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

import static com.kangwook.studyroom.global.common.StatusCode.FORBIDDEN_USER;
import static com.kangwook.studyroom.global.common.StatusCode.INVALID_TOKEN;

@Aspect
@Component
public class AuthAspect {

    @Around("@annotation(authRequired) && @within(org.springframework.web.bind.annotation.RestController)")
    public Object checkAuth(ProceedingJoinPoint joinPoint, AuthRequired authRequired) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");

        if (token == null || token.isBlank()) {
            throw new CustomException(INVALID_TOKEN);
        }

        Role role;
        Long userId = null;

        if (token.equals("admin-token")) {
            role = Role.ADMIN;
        } else if (token.matches("user-token-\\d+")) {
            role = Role.USER;
            userId = Long.parseLong(token.split("-")[2]);
        } else {
            throw new CustomException(INVALID_TOKEN);
        }


        Role[] allowedRoles = authRequired.value();
        if (!Arrays.asList(allowedRoles).contains(role)) {
            throw new CustomException(FORBIDDEN_USER);
        }

        request.setAttribute("role", role);
        request.setAttribute("userId", userId);


        return joinPoint.proceed();
    }
}

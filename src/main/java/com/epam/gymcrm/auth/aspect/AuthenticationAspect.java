package com.epam.gymcrm.auth.aspect;

import com.epam.gymcrm.auth.Authentication;
import com.epam.gymcrm.auth.annotation.Authenticated;
import com.epam.gymcrm.exception.AuthorizationException;
import com.epam.gymcrm.model.Role;
import com.epam.gymcrm.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthenticationAspect {

    private final JwtUtil jwtUtil;

    @Pointcut("@annotation(com.epam.gymcrm.auth.annotation.Authenticated) && args(.., token)")
    public void tokenPointcut(String token) {}

    @Before("tokenPointcut(token)")
    public void authenticate(JoinPoint joinPoint, String token) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Authenticated authenticated = signature.getMethod().getAnnotation(Authenticated.class);
        Role[] allowedRoles = authenticated.roles();

        Authentication authentication = jwtUtil.validateToken(token);
        if (allowedRoles.length > 0 && !hasRequiredRole(authentication, allowedRoles)) {
            throw new AuthorizationException();
        }
    }

    private boolean hasRequiredRole(Authentication authentication, Role[] allowedRoles) {
        Role userRole = authentication.role();
        for (Role role : allowedRoles) {
            if (userRole == role) {
                return true;
            }
        }
        return false;
    }
}

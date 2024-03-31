package com.epam.gymcrm.auth.aspect;

import com.epam.gymcrm.auth.annotation.Authenticated;
import com.epam.gymcrm.auth.Authentication;
import com.epam.gymcrm.exception.AuthenticationException;
import com.epam.gymcrm.exception.AuthorizationException;
import com.epam.gymcrm.model.Role;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthenticationAspect {

    private final Authentication authentication;

    @Before("@annotation(com.epam.gymcrm.auth.annotation.Authenticated)")
    public void authenticate(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Authenticated authenticated = signature.getMethod().getAnnotation(Authenticated.class);
        if (authentication.getRole() == null) {
            throw new AuthenticationException();
        }

        // Check if the user has the required role
        Role[] allowedRoles = authenticated.roles();
        if (allowedRoles.length > 0 && !hasRequiredRole(allowedRoles)) {
            throw new AuthorizationException();
        }
    }

    private boolean hasRequiredRole(Role[] allowedRoles) {
        Role userRole = authentication.getRole();
        for (Role role : allowedRoles) {
            if (userRole == role) {
                return true;
            }
        }
        return false;
    }
}

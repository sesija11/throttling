package com.throttling.task;

import com.throttling.task.access.CheckerAnnotation;
import com.throttling.task.controller.AccessController;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class CheckAccessAspect {
    private AccessController accessController;

    public CheckAccessAspect(AccessController accessController) {
        this.accessController = accessController;
    }

    @Before(value = "@annotation(checkerAnnotation)")
    public void check(CheckerAnnotation checkerAnnotation) throws AccessException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        if (!accessController.checkAccess(request.getRemoteAddr()))
            throw new AccessException();
    }
}

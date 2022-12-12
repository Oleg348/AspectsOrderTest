package org.example;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

@Aspect
@Order(2)
@Slf4j
public class LogRuntimeExceptionsAspect {

    @AfterThrowing(pointcut = "execution(* MyService.save(..))", throwing = "ex")
    public void log(RuntimeException ex) {
        log.error("some error", ex);
    }
}

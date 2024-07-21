package org.softuni.finalproject.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class GeneralControllerAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralControllerAspect.class);

    @Pointcut("execution(* org.softuni.finalproject.web..*.*(..))")
    public void controllerMethods() {
    }

    @Before("controllerMethods()")
    public void beforeExecutingMethod(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        LOGGER.debug("Executing method: {} With arguments: {}", joinPoint.getSignature().getName(), args);
    }

    @AfterThrowing(pointcut = "controllerMethods()", throwing = "exception")
    public void afterThrowingMethod(JoinPoint joinPoint, Throwable exception) {
        LOGGER.error("Error in: {}: {}", joinPoint.getSignature().getName(),
                exception.getMessage());
    }

    @Around("controllerMethods()")
    public Object debugExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            LOGGER.debug("Execution time of {}.{}: {} ms",
                    joinPoint.getTarget().getClass().getSimpleName(),
                    joinPoint.getSignature().getName(),
                    elapsedTime);
            return result;
        } catch (Throwable throwable) {
            long elapsedTime = System.currentTimeMillis() - start;
            LOGGER.error("Execution time of {}.{}: {} ms with exception: {}",
                    joinPoint.getTarget().getClass().getSimpleName(),
                    joinPoint.getSignature().getName(),
                    elapsedTime, throwable.getMessage(), throwable);
            throw throwable;
        }
    }

}

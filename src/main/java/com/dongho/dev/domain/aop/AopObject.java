package com.dongho.dev.domain.aop;

import lombok.Getter;
import lombok.Setter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@Aspect
@Component
public class AopObject {

    /**
     * Matches the execution of any public method in a type with the AopTest
     * annotation, or any subtype of a type with the AopTest annotation.
     */
    @Pointcut("execution(public * ((@AopTest *)+).*(..)) && within(@AopTest *)")
    private void executionOfAnyPublicMethodInAtAopTestType() {

    }

    /**
     * Matches the execution of any method with the AopTest annotation.
     */
    @Pointcut("execution(@AopTest * *(..))")
    private void executionOfAopTestMethod() {

    }


    @Before("executionOfAnyPublicMethodInAtAopTestType() || executionOfAopTestMethod()")
    public void beforeWoved(JoinPoint joinPoint) {
        Class<?> clazz = joinPoint.getTarget().getClass();
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();

        value.incrementAndGet();

        AopTest aopTestAnnotationOnClass = clazz.getAnnotation(AopTest.class);
        if (aopTestAnnotationOnClass == null) {
            foundAnnotationOnClass = false;
        }

        AopTest aopTestAnnotationOnMethod = method.getAnnotation(AopTest.class);
        if (aopTestAnnotationOnMethod == null) {
            foundAnnotationOnMethod = false;
        }
    }

    private AtomicInteger value = new AtomicInteger();
    private boolean foundAnnotationOnClass = true;
    private boolean foundAnnotationOnMethod = true;

    public void reset() {
        foundAnnotationOnClass = true;
        foundAnnotationOnMethod = true;
        value.set(0);
    }

}

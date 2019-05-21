package com.dongho.dev.domain.aop;

import lombok.Getter;
import lombok.Setter;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

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
    public void woved() {
        value.incrementAndGet();
    }

    private AtomicInteger value = new AtomicInteger();

    public void reset() {
        value.set(0);
    }

}

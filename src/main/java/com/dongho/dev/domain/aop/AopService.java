package com.dongho.dev.domain.aop;

public interface AopService {

    @AopTest
    void parentOnly();

    void childOnly();

    @AopTest
    void parentChildBoth();

}

package com.dongho.dev.domain.aop;

import org.springframework.stereotype.Service;

@Service
public class AopServiceImpl implements AopService {

    @Override
    public void parentOnly() {

    }

    @Override
    @AopTest
    public void childOnly() {

    }

    @Override
    @AopTest
    public void parentChildBoth() {

    }

}

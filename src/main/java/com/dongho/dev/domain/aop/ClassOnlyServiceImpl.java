package com.dongho.dev.domain.aop;

import org.springframework.stereotype.Service;

@AopTest
@Service
public class ClassOnlyServiceImpl implements ClassOnlyService {

    @Override
    public void foo() {

    }

}

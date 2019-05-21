package com.dongho.dev.domain.aop;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ClassOnlyServiceCglibProxyTest {

    @Autowired
    private AopObject aopObject;

    @Autowired
    private ClassOnlyService classOnlyService;

    @Autowired
    private ClassOnlyServiceImpl classOnlyServiceImpl;

    @Before
    public void beforeTest() {
        aopObject.reset();
    }

    @Test
    public void springDiTest() {
        assertThat(classOnlyService).isNotNull();
        assertThat(classOnlyServiceImpl).isNotNull();

        assertThat(AopUtils.isCglibProxy(classOnlyService)).isTrue();
        assertThat(AopUtils.isCglibProxy(classOnlyServiceImpl)).isTrue();
    }

    @Test
    public void classOnlyServiceTest() {
        given: {
        }

        when: {
            classOnlyService.foo();
        }

        then: {
            assertThat(aopObject.getValue().get()).isEqualTo(1);
        }
    }

}

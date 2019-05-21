package com.dongho.dev.domain.aop;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
    "spring.aop.proxy-target-class=false"
})
@RunWith(SpringRunner.class)
public class AopServiceWithJdkProxyTest {

    @Autowired
    private AopObject aopObject;

    @Autowired
    private AopService aopService;

    @Autowired(required = false)
    private AopServiceImpl aopServiceImpl;

    @Before
    public void beforeTest() {
        aopObject.reset();
    }

    @Test
    public void springDiTest() {
        assertThat(aopService).isNotNull();
        assertThat(aopServiceImpl).isNull();

        assertThat(AopUtils.isJdkDynamicProxy(aopService)).isTrue();
    }

    @Test
    public void parentOnlyTest() {
        given: {
        }

        when: {
            aopService.parentOnly();
        }

        then: {
            // Not Proxied.
            assertThat(aopObject.getValue().get()).isEqualTo(0);
        }
    }

    @Test
    public void childOnlyTest() {
        given: {
        }

        when: {
            aopService.childOnly();
        }

        then: {
            assertThat(aopObject.getValue().get()).isEqualTo(1);
            assertThat(aopObject.isFoundAnnotationOnClass()).isFalse();

            // Can't find annotation info from interface method.
            assertThat(aopObject.isFoundAnnotationOnMethod()).isFalse();
        }
    }

    @Test
    public void parentChildBothTest() {
        given: {
        }

        when: {
            aopService.parentChildBoth();
        }

        then: {
            assertThat(aopObject.getValue().get()).isEqualTo(1);
            assertThat(aopObject.isFoundAnnotationOnClass()).isFalse();

            // Can find annotation info from interface method using JDK proxy.
            assertThat(aopObject.isFoundAnnotationOnMethod()).isTrue();
        }
    }

}

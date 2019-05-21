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
public class AopServiceWithCglibProxyTest {

    @Autowired
    private AopObject aopObject;

    @Autowired
    private AopService aopService;

    @Autowired
    private AopServiceImpl aopServiceImpl;

    @Before
    public void beforeTest() {
        aopObject.reset();
    }

    @Test
    public void springDiTest() {
        assertThat(aopService).isNotNull();
        assertThat(aopServiceImpl).isNotNull();

        assertThat(AopUtils.isCglibProxy(aopService)).isTrue();
        assertThat(AopUtils.isCglibProxy(aopServiceImpl)).isTrue();
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

            // Can find annotation info from child concrete class's method using CGLib.
            assertThat(aopObject.isFoundAnnotationOnMethod()).isTrue();
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

            // Can find annotation info from child concrete class's method using CGLib.
            assertThat(aopObject.isFoundAnnotationOnMethod()).isTrue();
        }
    }

    @Test
    public void noProxyTest() {
        given: {
        }

        when: {
            aopService.noProxy();
        }

        then: {
            // Not Proxied.
            assertThat(aopObject.getValue().get()).isEqualTo(0);
        }
    }

}

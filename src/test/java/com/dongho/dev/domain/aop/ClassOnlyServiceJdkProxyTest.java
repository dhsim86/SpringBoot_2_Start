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
public class ClassOnlyServiceJdkProxyTest {

    @Autowired
    private AopObject aopObject;

    @Autowired
    private ClassOnlyService classOnlyService;

    @Autowired(required = false)
    private ClassOnlyServiceImpl classOnlyServiceImpl;

    @Before
    public void beforeTest() {
        aopObject.reset();
    }

    @Test
    public void springDiTest() {
        assertThat(classOnlyService).isNotNull();
        assertThat(classOnlyServiceImpl).isNull();

        assertThat(AopUtils.isJdkDynamicProxy(classOnlyService)).isTrue();
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

            // Can find annotation info from target.
            assertThat(aopObject.isFoundAnnotationOnClass()).isTrue();

            assertThat(aopObject.isFoundAnnotationOnMethod()).isFalse();
        }
    }

}

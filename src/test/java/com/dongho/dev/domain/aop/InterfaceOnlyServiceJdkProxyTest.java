package com.dongho.dev.domain.aop;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
    "spring.aop.proxy-target-class=false"
})
@RunWith(SpringRunner.class)
public class InterfaceOnlyServiceJdkProxyTest {

    @Autowired
    private AopObject aopObject;

    @Autowired
    private InterfaceOnlyService interfaceOnlyService;

    @Autowired
    private InterfaceOnlyServiceImpl interfaceOnlyServiceImpl;

    @Before
    public void beforeTest() {
        aopObject.reset();
    }

    @Test
    public void springDiTest() {
        // Not proxied.
        assertThat(interfaceOnlyService).isNotNull();
        assertThat(interfaceOnlyServiceImpl).isNotNull();
    }

    @Test
    public void interfaceOnlyServiceTest() {
        given: {
        }

        when: {
            interfaceOnlyService.foo();
        }

        then: {
            assertThat(aopObject.getValue().get()).isEqualTo(0);
        }
    }

}

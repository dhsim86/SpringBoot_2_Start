package com.dongho.dev.domain.aop;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.ProxyUtils;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class InterfaceOnlyServiceCglibProxyTest {

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
        assertThat(interfaceOnlyService).isNotNull();
        assertThat(interfaceOnlyServiceImpl).isNotNull();

        // Not proxied.
        assertThat(AopUtils.isAopProxy(interfaceOnlyService)).isFalse();
        assertThat(AopUtils.isAopProxy(interfaceOnlyServiceImpl)).isFalse();
    }

    @Test
    public void interfaceOnlyServiceTest() {
        given:
        {
        }

        when:
        {
            interfaceOnlyService.foo();
        }

        then:
        {
            assertThat(aopObject.getValue().get()).isEqualTo(0);
        }
    }

}

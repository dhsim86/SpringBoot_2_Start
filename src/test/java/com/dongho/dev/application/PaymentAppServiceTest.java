package com.dongho.dev.application;

import com.dongho.dev.config.FixtureConfig;
import com.dongho.dev.domain.payment.Payment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.temporal.ChronoField;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@Import(FixtureConfig.class)
public class PaymentAppServiceTest {

    @Autowired
    private PaymentAppService paymentAppService;

    @Test(expected = NoSuchElementException.class)
    public void test() {
        // given

        // when
        Payment payment = paymentAppService.approve(1000);

        // then
        assertThat(payment).isNull();
    }


    @Test
    public void testWithoutTransactional() {
        // given

        // when
        Payment payment = paymentAppService.approveWithoutTransactional(1000);

        // then
        assertThat(payment).isNotNull();
        assertThat(payment.getApprovedAt().get(ChronoField.MILLI_OF_SECOND)).isEqualTo(0);
    }

}

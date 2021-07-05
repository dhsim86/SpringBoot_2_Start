package com.dongho.dev.domain.payment;

import com.dongho.dev.config.FixtureConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@Import(FixtureConfig.class)
public class PaymentRepositoryTest {

    @Autowired
    private FixtureConfig fixtureConfig;

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    public void saveTest() {
        // given
        Payment payment = fixtureConfig.getPayment();

        // when
        paymentRepository.save(payment);

        // then
        assertThat(payment.getId()).isNotNull();
    }

    @Test
    @Transactional
    public void convertTransactionalTest() {
        // given
        Payment payment = fixtureConfig.getPayment();
        LocalDateTime approvedAt = LocalDateTime.now().with(ChronoField.MILLI_OF_SECOND, 111);
        payment.setApprovedAt(approvedAt);

        // when
        paymentRepository.save(payment);
        Payment result = paymentRepository.findById(payment.getId()).orElseThrow(() -> new RuntimeException());

        // then
        assertThat(payment.getId()).isNotNull();
        assertThat(payment.getApprovedAt().get(ChronoField.MILLI_OF_SECOND)).isNotEqualTo(0);
        assertThat(result.getCreatedAt().get(ChronoField.MILLI_OF_SECOND)).isNotEqualTo(0);
        assertThat(result.getUpdatedAt().get(ChronoField.MILLI_OF_SECOND)).isNotEqualTo(0);
        assertThat(result.getApprovedAt().get(ChronoField.MILLI_OF_SECOND)).isNotEqualTo(0);
    }

    @Test
    public void convertTest() {
        // given
        Payment payment = fixtureConfig.getPayment();
        LocalDateTime approvedAt = LocalDateTime.now().with(ChronoField.MILLI_OF_SECOND, 111);
        payment.setApprovedAt(approvedAt);

        // when
        paymentRepository.save(payment);
        Payment result = paymentRepository.findById(payment.getId()).orElseThrow(() -> new RuntimeException());

        // then
        assertThat(payment.getId()).isNotNull();
        assertThat(payment.getApprovedAt().get(ChronoField.MILLI_OF_SECOND)).isNotEqualTo(0);
        assertThat(result.getCreatedAt().get(ChronoField.MILLI_OF_SECOND)).isEqualTo(0);
        assertThat(result.getUpdatedAt().get(ChronoField.MILLI_OF_SECOND)).isEqualTo(0);
        assertThat(result.getApprovedAt().get(ChronoField.MILLI_OF_SECOND)).isEqualTo(0);
    }

}

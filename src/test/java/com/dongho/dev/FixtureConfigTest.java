package com.dongho.dev;

import com.dongho.dev.config.FixtureConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@Import(FixtureConfig.class)
public class FixtureConfigTest {

    @Autowired
    private FixtureConfig fixtureConfig;

    @Test
    public void fixtureProperty() {
        assertThat(fixtureConfig.getPayment().getMainMethodAmount() ).isEqualTo(BigDecimal.valueOf(1000));
    }

}

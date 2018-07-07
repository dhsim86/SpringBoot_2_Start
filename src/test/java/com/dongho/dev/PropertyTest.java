package com.dongho.dev;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@Import(PropertyTestConfig.class)
public class PropertyTest {

	@Autowired
	private PropertyTestConfig config;

	@Test
	public void testProperty() {

		given: {

		}

		when: {

		}

		then: {
			assertThat(config.getTest()).isEqualTo("test");
		}
	}
}

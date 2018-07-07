package com.dongho.dev.config;

import com.dongho.dev.domain.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserConfigTest {

	@Autowired
	private UserService userService;

	@Test
	public void xmlConfigTest() {

		assertThat(userService).isNotNull();
	}
}

package com.dongho.dev;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Data
@Validated
@TestConfiguration
@ConfigurationProperties(prefix = "fixtures")
public class PropertyTestConfig {

	@NotEmpty
	private String test;
}

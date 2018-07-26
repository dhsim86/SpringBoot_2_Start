package com.dongho.dev.web;

import com.dongho.dev.web.protocol.Message;
import com.dongho.dev.web.protocol.StatementParameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class HelloController {

	@GetMapping("/")
	public Flux<Message> getMessage(StatementParameter parameter) {
		return Flux.just(
			Message.builder().body("Hello Spring 5").build(),
			Message.builder().body("Hello Spring Boot 2").build()
		);
	}
}

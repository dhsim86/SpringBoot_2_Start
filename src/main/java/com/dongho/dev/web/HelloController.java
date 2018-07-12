package com.dongho.dev.web;

import com.dongho.dev.domain.user.UserService;
import com.dongho.dev.web.protocol.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class HelloController {

	@Autowired
	private UserService userService;

	@GetMapping("/")
	public Flux<Message> getMessage() {
		return Flux.just(
			Message.builder().body("Hello Spring 5").build(),
			Message.builder().body("Hello Spring Boot 2").build()
		);
	}

	@GetMapping("/users")
	public Mono<String> getUser(@RequestParam("id") String id) {
		return Mono.just(userService.getUserName(id));
	}

	@DeleteMapping("/users")
	public Mono<String> deleteUser(@RequestParam("id") String id) {
		userService.deleteUser(id);
		return Mono.just("success");
	}
}

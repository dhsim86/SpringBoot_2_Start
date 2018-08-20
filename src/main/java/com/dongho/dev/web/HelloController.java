package com.dongho.dev.web;

import com.dongho.dev.domain.user.UserService;
import com.dongho.dev.web.protocol.Message;
import com.dongho.dev.web.protocol.StatementParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RestController
public class HelloController {

    @Autowired
    private UserService userService;

	@GetMapping("/")
	public Flux<Message> getMessage(StatementParameter parameter) {
		return Flux.just(
			Message.builder().body("Hello Spring 5").build(),
			Message.builder().body("Hello Spring Boot 2").build()
		);
	}

    @GetMapping("/noton")
    public Mono<String> getNoton() {
        log.info("{}: start", Thread.currentThread().getName());
        return Mono.fromCallable(() -> "publish")
                .doOnNext(str -> log.info("{}: first", Thread.currentThread().getName()))
                .flatMap(str -> Mono.defer(() -> {
                    log.info("{}: before get user name", Thread.currentThread().getName());
                    return userService.getUserName();
                }))
                .zipWith(Mono.defer(() -> Mono.fromCallable(() -> {
                    log.info("{}: zipwith", Thread.currentThread().getName());
                    return "zipwith";
                })))
                .map(tuple -> {
                    log.info("{}: tuple map", Thread.currentThread().getName());
                    return tuple.getT1() + tuple.getT2();
                });
    }

	@GetMapping("/subscribeon")
    public Mono<String> getSubsbribeOn() {
        log.info("{}: start", Thread.currentThread().getName());
        return Mono.fromCallable(() -> "publish")
                .subscribeOn(Schedulers.elastic())
                .doOnNext(str -> log.info("{}: first", Thread.currentThread().getName()))
                .flatMap(str -> Mono.defer(() -> {
                    log.info("{}: before get user name", Thread.currentThread().getName());
                    return userService.getUserName();
                }))
                .zipWith(Mono.defer(() -> Mono.fromCallable(() -> {
                    log.info("{}: zipwith", Thread.currentThread().getName());
                    return "zipwith";
                })))
                .map(tuple -> {
                    log.info("{}: tuple map", Thread.currentThread().getName());
                    return tuple.getT1() + tuple.getT2();
                });
    }

    @GetMapping("/publishon")
    public Mono<String> getPublishOn() {
        log.info("{}: start", Thread.currentThread().getName());
        return Mono.fromCallable(() -> "publish")
                .publishOn(Schedulers.elastic())
                .doOnNext(str -> log.info("{}: first", Thread.currentThread().getName()))
                .flatMap(str -> Mono.defer(() -> {
                    log.info("{}: before get user name", Thread.currentThread().getName());
                    return userService.getUserName();
                }))
                .zipWith(Mono.defer(() -> Mono.fromCallable(() -> {
                    log.info("{}: zipwith", Thread.currentThread().getName());
                    return "zipwith";
                })))
                .map(tuple -> {
                    log.info("{}: tuple map", Thread.currentThread().getName());
                    return tuple.getT1() + tuple.getT2();
                });
    }

    @GetMapping("/mixon")
    public Mono<String> getMixOn() {
        log.info("{}: start", Thread.currentThread().getName());
        return Mono.fromCallable(() -> "publish")
                .subscribeOn(Schedulers.elastic())
                .doOnNext(str -> log.info("{}: first", Thread.currentThread().getName()))
                .publishOn(Schedulers.elastic())
                .flatMap(str -> Mono.defer(() -> {
                    log.info("{}: before get user name", Thread.currentThread().getName());
                    return userService.getUserName();
                }))
                .publishOn(Schedulers.parallel())
                .zipWith(Mono.defer(() -> Mono.fromCallable(() -> {
                    log.info("{}: zipwith", Thread.currentThread().getName());
                    return "zipwith";
                })))
                .map(tuple -> {
                    log.info("{}: tuple map", Thread.currentThread().getName());
                    return tuple.getT1() + tuple.getT2();
                });
    }
}

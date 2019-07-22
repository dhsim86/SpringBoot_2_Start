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
import reactor.core.scheduler.Schedulers;

@Slf4j
@RestController
public class HelloController {

    @Autowired
    private UserService userService;

	@GetMapping("/")
	public Flux<Message> getMessage(StatementParameter parameter) {
	    log.info("Got Message");
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

    @GetMapping("/notonWithMonoZip")
    public Mono<String> notonWithMonoZip() {
        log.info("{}: start", Thread.currentThread().getName());
        return Mono.fromCallable(() -> "publish")
                .doOnNext(str -> log.info("{}: first", Thread.currentThread().getName()))
                .flatMap(str -> Mono.defer(() -> {
                    log.info("{}: before get user name", Thread.currentThread().getName());
                    return userService.getUserName();
                }))
                .flatMap(str ->
                    Mono.zip(Mono.fromCallable(() -> "noton").doOnNext(t1 -> log.info("{}: t1", Thread.currentThread().getName())),
                            Mono.fromCallable(() -> "WithMonoZip").doOnNext(t1 -> log.info("{}: t2", Thread.currentThread().getName())),
                            (t1, t2) -> {
                                log.info("{}: t1+t2", Thread.currentThread().getName());
                                return t1 + t2;
                            })
                );
    }


    @GetMapping("/elasticMonoZip")
    public Mono<String> onWithMonoZip() {
        log.info("{}: start", Thread.currentThread().getName());
        return Mono.fromCallable(() -> "publish")
                .doOnNext(str -> log.info("{}: first", Thread.currentThread().getName()))
                .flatMap(str -> Mono.defer(() -> {
                    log.info("{}: before get user name", Thread.currentThread().getName());
                    return userService.getUserName();
                }))
                .flatMap(str ->
                        Mono.zip(Mono.fromCallable(() -> "noton")
                                    .subscribeOn(Schedulers.elastic())
                                    .doOnNext(t1 -> log.info("{}: t1", Thread.currentThread().getName())),
                                Mono.fromCallable(() -> "WithMonoZip")
                                    .subscribeOn(Schedulers.elastic())
                                    .doOnNext(t1 -> log.info("{}: t2", Thread.currentThread().getName())),
                                (t1, t2) -> {
                                    log.info("{}: t1+t2", Thread.currentThread().getName());
                                    return t1 + t2;
                                })
                );
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

    private String nullReturn() {
        log.info("nullReturn");
        return null;
    }

    private Mono<String> trueString() {
        log.info("trueString");
        return Mono.just("true");
    }

    private Mono<String> falseString() {
        log.info("falseString");
        return Mono.just("false");
    }

    @GetMapping("/mono/switchIfEmptyNull")
    public Mono<String> switchIfEmptyNullTest() {
        Mono.fromCallable(() -> nullReturn())
            .switchIfEmpty(falseString())
            .subscribe();

        // falseString
        // trueString

        return Mono.empty();
    }

    @GetMapping("/mono/switchIfEmptyTrue")
    public Mono<String> switchIfEmptyFilterTest() {
        boolean is = true;

        Mono.fromCallable(() -> is)
                .filter(Boolean::booleanValue)
                .flatMap(b -> trueString())
                .switchIfEmpty(falseString())
                .subscribe();

        // falseString
        // trueString

        return Mono.empty();
    }

}

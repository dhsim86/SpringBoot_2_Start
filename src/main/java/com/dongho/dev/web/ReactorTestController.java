package com.dongho.dev.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RestController
@RequestMapping("/reactor")
public class ReactorTestController {

    @GetMapping("/publishon")
    public Mono<String> reactor() {
        Flux.range(1, 10)
            .publishOn(Schedulers.newSingle("pub"))
            .log()
            .subscribeOn(Schedulers.newSingle("sub"))
            .subscribe(n -> log.info("{}", n));

        return Mono.empty();
    }

    @GetMapping("/publishon2")
    public Mono<String> reactor2() {
        Flux.defer(() -> {
            log.info("Defer");
            return Flux.range(1, 10);
        }).publishOn(Schedulers.newSingle("pub"))
            .log()
            .subscribeOn(Schedulers.newSingle("sub"))
            .subscribe(n -> log.info("{}", n));

        return Mono.empty();
    }



}

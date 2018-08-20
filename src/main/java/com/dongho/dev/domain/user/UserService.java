package com.dongho.dev.domain.user;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class UserService {

    public Mono<String> getUserName() {
        return Mono.fromCallable(() -> {
            Thread.sleep(1000);
            log.info("{}: getUserName", Thread.currentThread().getName());
            return "userName";
        });
    }
}

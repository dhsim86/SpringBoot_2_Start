package com.dongho.dev.web;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/publisher2")
public class PublisherTestController2 {

    private Publisher pub = subscriber -> subscriber.onSubscribe(new Subscription() {
        @Override
        public void request(long n) {
            log.info("request - {}", n);

            subscriber.onNext(1);
            subscriber.onNext(2);
            subscriber.onComplete();
        }

        @Override
        public void cancel() {

        }
    });

    private Subscriber sub = new Subscriber() {
        @Override
        public void onSubscribe(Subscription s) {
            log.info("onSubscribe");
            s.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(Object o) {
            log.info("onNext - {}", o);
        }

        @Override
        public void onError(Throwable t) {

        }

        @Override
        public void onComplete() {
            log.info("onComplete");
        }
    };

    private Publisher pubOn = subscriber ->
        Executors.newSingleThreadScheduledExecutor().execute(() -> pub.subscribe(subscriber));

    private Subscriber subOn = new Subscriber() {
        @Override
        public void onSubscribe(Subscription s) {
            Executors.newSingleThreadScheduledExecutor().execute(() -> sub.onSubscribe(s));
        }

        @Override
        public void onNext(Object o) {
            Executors.newSingleThreadScheduledExecutor().execute(() -> sub.onNext(o));
        }

        @Override
        public void onError(Throwable t) {
            Executors.newSingleThreadScheduledExecutor().execute(() -> sub.onError(t));
        }

        @Override
        public void onComplete() {
            Executors.newSingleThreadScheduledExecutor().execute(() -> sub.onComplete());
        }
    };

    @GetMapping("/test1")
    public Mono<String> test1() {
        log.info("Start {}", Thread.currentThread().getName());

        pub.subscribe(sub);
        return Mono.empty();
    }

    @GetMapping("/publishOn")
    public Mono<String> publishOn() {
        log.info("Start {}", Thread.currentThread().getName());

        pubOn.subscribe(sub);
        return Mono.empty();
    }

    @GetMapping("/all")
    public Mono<String> all() {
        log.info("Start {}", Thread.currentThread().getName());

        pubOn.subscribe(subOn);
        return Mono.empty();
    }

    @GetMapping("/reactor")
    public Mono<String> reactor() {
        Flux.range(1, 10)
            .publishOn(Schedulers.newSingle("pub"))
            .log()
            .subscribeOn(Schedulers.newSingle("sub"))
            .subscribe(n -> log.info("{}", n));

        return Mono.empty();
    }

    @GetMapping("/reactor2")
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

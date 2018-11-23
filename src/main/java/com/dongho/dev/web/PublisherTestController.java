package com.dongho.dev.web;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Stack;

@Slf4j
@RestController
@RequestMapping("/publisher")
public class PublisherTestController {

    @GetMapping("/test1")
    public Mono<Void> test1() {
        Publisher publisher = new Publisher() {

            private Stack stack;

            @Override
            public void subscribe(Subscriber subscriber) {
                stack = new Stack();

                for (int i = 0; i < 10; i++) {
                    stack.push(i);
                }

                subscriber.onSubscribe(new Subscription() {

                    @Override
                    public void request(long n) {
                        log.info("request: {}", n);

                        if (n < 0) {
                            subscriber.onError(new Exception("subscription error"));
                        }

                        for (int i = 1; i <= n; i++) {
                            if (stack.empty()) {
                                subscriber.onComplete();
                                return;
                            }

                            subscriber.onNext(stack.pop());
                        }
                    }

                    @Override
                    public void cancel() {

                    }
                });

            }

        };

        Subscriber subscriber = new Subscriber() {

            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                subscription.request(1);
            }

            @Override
            public void onNext(Object o) {
                log.info("onNext: {}", o);
                subscription.request(1);
            }

            @Override
            public void onError(Throwable t) {
                log.error("onError: {}", t.getMessage());
            }

            @Override
            public void onComplete() {
                log.info("onComplete");
            }
        };

        publisher.subscribe(subscriber);

        return Mono.empty();
    }

}

package com.dongho.dev.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

@Slf4j
@RestController
@RequestMapping("/observable")
public class ObservableTestController {

    public static class Observable1 extends Observable {

        public void say() {

            List dataArr = Arrays.asList(1, 2, 3, 4, 5);

            for (Object i : dataArr) {
                setChanged();
                notifyObservers(i);
            }

        }

    }

    @GetMapping("/test1")
    public Mono<Void> test1() {
        Observable1 observable = new Observable1();

        Observer observer1 = (o, data) -> log.info("update 1: {}", data);
        Observer observer2 = (o, data) -> log.info("update 2: {}", data);

        observable.addObserver(observer1);
        observable.addObserver(observer2);
        observable.say();

        return Mono.empty();
    }

}

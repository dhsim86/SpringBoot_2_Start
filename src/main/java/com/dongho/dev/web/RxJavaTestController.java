package com.dongho.dev.web;

import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/rxjava")
public class RxJavaTestController {

    @GetMapping("/test1")
    public Mono<String> test1() {
        Observable.just("Hello World!").subscribe(log::info);
        return Mono.empty();
    }

    @GetMapping("/test2")
    public Mono<String> test2() {
        Observable.just("Hello Wrold!").subscribe(s -> log.info("[{}] {}", System.currentTimeMillis(), s));
        return Mono.empty();
    }

    @GetMapping("/test3")
    public Mono<String> test3() {
        Observable.just("Hello World!")
            .map(s -> "[" + System.currentTimeMillis() + "]" + s)
            .subscribe(log::info);
        return Mono.empty();
    }

    @GetMapping("/test4")
    public Mono<String> test4() {
        Observable.just("Hello World!")
            .map(s -> "[" + System.currentTimeMillis() + "]" + s)
            .map(s -> s.length())
            .subscribe(s -> log.info("{}", s));
        return Mono.empty();
    }

    public static Observable<List<String>> query(String text) {
        return Observable.just(Arrays.asList("www.naver.com", "www.google.com", "www.kakao.com"));
    }

    @GetMapping("/test5")
    public Mono<String> test5() {
        query("test")
            .subscribe(urls -> Observable.fromIterable(urls).subscribe(log::info));
        return Mono.empty();
    }

    @GetMapping("/test6")
    public Mono<String> test6() {
        query("test")
            .flatMap(urls -> Observable.fromIterable(urls))
            .subscribe(log::info);
        return Mono.empty();
    }

    @GetMapping("/test7")
    public Mono<String> test7() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        Observable.create(emitter -> {
            Future<Object> future = executor.schedule(() -> {
                emitter.onNext("emit 1");
                emitter.onNext("emit 2");
                emitter.onError(new Throwable());
                return null;
            }, 1, TimeUnit.SECONDS);

            emitter.setCancellable(() -> future.cancel(false));
        })
            .onErrorReturn(e -> "return")
            .subscribe(s -> log.info("on next: {}", s),
                       e -> log.error("on error:", e),
                       () -> log.info("completed"));

        return Mono.empty();
    }

    @GetMapping("/test8")
    public Mono<String> test8() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        Observable.create(emitter -> {
            Future<Object> future = executor.schedule(() -> {
                emitter.onNext("emit 1");
                emitter.onNext("emit 2");
                emitter.onError(new Throwable());
                return null;
            }, 1, TimeUnit.SECONDS);

            emitter.setCancellable(() -> future.cancel(false));
        })
            .onErrorResumeNext(Observable.fromArray("resume 1", "resume 2"))
            .subscribe(s -> log.info("on next: {}", s),
                       e -> log.error("on error:", e),
                       () -> log.info("completed"));

        return Mono.empty();
    }

}

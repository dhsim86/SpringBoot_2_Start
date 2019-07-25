package com.dongho.dev.web;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.adapter.rxjava.RxJava2Adapter;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    @GetMapping("/maybe")
    public Mono<String> maybe() {
        Maybe mayBe = Maybe.fromCallable(() -> "Maybe Test");
        return RxJava2Adapter.maybeToMono(mayBe);
    }

    @GetMapping("/maybeNull")
    public Mono<String> maybeNull() {
        //onSubscribe (onSuccess | onError | onComplete)?

        Maybe.fromCallable(() -> "Maybe Test")
            .subscribe(s -> log.info("[Maybe] on success: {}", s),      // onSuccess
                       e -> log.error("[Maybe] on error:", e),
                       () -> log.info("[Maybe] completed"));

        Maybe.fromCallable(() -> null)
            .subscribe(s -> log.info("[Maybe Null] on success: {}", s),
                       e -> log.error("[Maybe Null] on error:", e),
                       () -> log.info("[Maybe Null] completed"));       // onComplete

        return Mono.empty();
    }

    @GetMapping("/single")
    public Mono<String> single() {
        // onSubscribe (onSuccess | onError)?

        Single.fromCallable(() -> "Single Test")
            .subscribe(s -> log.info("[Single] on success: {}", s),     // onSuccess
                       e -> log.error("[Single] on error:", e));

        Single.fromCallable(() -> null)
            .subscribe(s -> log.info("[Single Null] on success: {}", s),
                       e -> log.error("[Single Null] on error:", e));   // onError

        return Mono.empty();
    }

    private Single<String> trueString() {
        log.info("trueString");
        return Single.just("true");
    }

    private Single<String> falseString() {
        log.info("falseString");
        return Single.just("false");
    }

    @GetMapping("/maybe/switchIfEmptyTrue")
    public Mono<String> switchIfEmptyTest() {
        boolean is = true;

        Single.fromCallable(() -> is)
            .filter(Boolean::booleanValue)
            .flatMap(b -> trueString().toMaybe())
            .switchIfEmpty(falseString())
            .subscribe();

        // falseString
        // trueString

        return Mono.empty();
    }

    @GetMapping("/maybe/switchIfEmptyTrueDefer")
    public Mono<String> switchIfEmptyDeferTest() {
        boolean is = true;

        Single.fromCallable(() -> is)
            .filter(Boolean::booleanValue)
            .flatMap(b -> trueString().toMaybe())
            .switchIfEmpty(Single.defer(() -> falseString()))
            .subscribe();

        // trueString

        return Mono.empty();
    }

    @GetMapping("/maybe/switchIfEmptyFalse")
    public Mono<String> switchIfEmptyFalseTest() {
        boolean is = false;

        Single.fromCallable(() -> is)
            .filter(Boolean::booleanValue)
            .flatMap(b -> trueString().toMaybe())
            .switchIfEmpty(falseString())
            .subscribe();

        // falseString

        return Mono.empty();
    }

    private List<String> stringList() {
        log.info("stringList");
        return Arrays.asList("test", "test2");
    }

    @GetMapping("/flowable/iterable")
    public Mono<String> flowableIterableTest() {

        Single.fromCallable(() -> 1)
            .doOnSuccess(v -> log.info("before"))
            .flatMapPublisher(v -> Flowable.fromIterable(stringList()))
            .doOnNext(v -> log.info(v))
            .subscribe();

        return Mono.empty();
    }

    @GetMapping("/flowable/fromSingleZip")
    public Mono<String> flowableFromSingleZipTest() {

        Single.fromCallable(() -> 1)
            .doOnSuccess(v -> log.info("before"))
            .flatMap(v -> Single.zip(Flowable.fromIterable(stringList()).toList(),
                                     Single.fromCallable(() -> 1),
                                     (list, v2) -> true))
            .subscribe();

        // before
        // stringList

        return Mono.empty();
    }

    @GetMapping("/flowable/fromSingleZipFilter")
    public Mono<String> flowableFromSingleZipFilterTest() {
        boolean is = true;

        Single.fromCallable(() -> is)
            .doOnSuccess(v -> log.info("before"))
            .filter(Boolean::booleanValue)
            .switchIfEmpty(Single.zip(Flowable.fromIterable(stringList()).toList(),
                                      Single.fromCallable(() -> 1),
                                      (list, v2) -> true))
            .subscribe();

        // stringList
        // before

        return Mono.empty();
    }

    @GetMapping("/filterTest/true")
    public Mono<String> filterTest() {
        boolean is = true;

        Single.fromCallable(() -> is)
            .filter(Boolean::booleanValue)
            .flatMap(b -> Single.fromCallable(() -> "test")
                .doOnSuccess(s -> log.info("doOnSuccess: {}", s)).toMaybe())
            .switchIfEmpty(Single.defer(() -> falseString()))
            .doOnSuccess(s -> log.info("result: {}", s))
            .subscribe();

        // doOnSuccess: test
        // result: test

        return Mono.empty();
    }

    @GetMapping("/filterTest/false")
    public Mono<String> filterTestFalse() {
        boolean is = false;

        Single.fromCallable(() -> is)
            .filter(Boolean::booleanValue)
            .flatMap(b -> Single.fromCallable(() -> "test")
                .doOnSuccess(s -> log.info("doOnSuccess: {}", s)).toMaybe())
            .switchIfEmpty(Single.defer(() -> falseString()))
            .doOnSuccess(s -> log.info("result: {}", s))
            .subscribe();

        // falseString
        // result: false

        return Mono.empty();
    }

    @GetMapping("/filterTest/map")
    public Mono<String> filterTestMapForm() {
        boolean is = false;

        Single.fromCallable(() -> is)
            .filter(Boolean::booleanValue)
            .map(b -> "test")
            .doOnSuccess(s -> log.info("doOnSuccess: {}", s))
            .switchIfEmpty(Single.defer(() -> falseString()))
            .doOnSuccess(s -> log.info("result: {}", s))
            .subscribe();

        // falseString
        // result: false

        return Mono.empty();

    }

    @GetMapping("/filterTest/maybeFlatMap")
    public Mono<String> filterTestMaybeFlatMapForm() {
        boolean is = false;

        Single.fromCallable(() -> is)
            .filter(Boolean::booleanValue)
            .flatMap(b -> Single.fromCallable(() -> "test").toMaybe())
            .doOnSuccess(s -> log.info("doOnSuccess: {}", s))
            .switchIfEmpty(Single.defer(() -> falseString()))
            .doOnSuccess(s -> log.info("result: {}", s))
            .subscribe();

        // falseString
        // result: false

        return Mono.empty();

    }

    @GetMapping("/filterTest/FlowableFlatMap")
    public Mono<String> filterTestFlowableFlatMapForm() {
        boolean is = false;

        Single.fromCallable(() -> is)
            .filter(Boolean::booleanValue)
            .flatMap(b -> Flowable.fromIterable(Arrays.asList("1", "2", "3")).toList().toMaybe())
            .doOnSuccess(s -> log.info("doOnSuccess: {}", s))
            .switchIfEmpty(Single.defer(() -> falseString().toFlowable().toList()))
            .doOnSuccess(s -> log.info("result: {}", s))
            .subscribe();

        // falseString
        // result: [false]

        return Mono.empty();

    }

    @GetMapping("/concatArrayTest/delay")
    public Mono<String> concatArrayTestForDelay() {

        Single.concatArray(Single.fromCallable(() -> 1)
                               .doOnSuccess(n -> log.info("first emit before delay {}", n))
                               .delay(5, TimeUnit.SECONDS)
                               .doOnSuccess(n -> log.info("first emit after delay {}", n)),
                           Single.fromCallable(() -> 2)
                               .doOnSuccess(n -> log.info("second emit before delay {}", n))
                               .delay(5, TimeUnit.SECONDS)
                               .doOnSuccess(n -> log.info("second emit after delay {}", n)))
            .doOnNext(n -> log.info("after concat array. {}", n))
            .subscribe();

        return Mono.empty();
    }

    @GetMapping("/concatArrayTest/delay2")
    public Mono<String> concatArrayTestForDelay2() {

        Single.concatArray(Single.fromCallable(() -> true)
                               .doOnSuccess(n -> log.info("first emit {}", n))
                               .delay(5, TimeUnit.SECONDS),
                           Single.fromCallable(() -> true)
                               .doOnSuccess(n -> log.info("second emit {}", n)))
            .all(Boolean::booleanValue)
            .doOnSuccess(result -> log.info("result: {}", result))
            .subscribe();

        return Mono.empty();
    }

    @GetMapping("/zipTest/delay")
    public Mono<String> zipTestForDelay() {

        Single.zip(Single.fromCallable(() -> 1)
                       .doOnSuccess(n -> log.info("first emit before delay {}", n))
                       .delay(5, TimeUnit.SECONDS)
                       .doOnSuccess(n -> log.info("first emit after delay {}", n)),
                   Single.fromCallable(() -> 2)
                       .doOnSuccess(n -> log.info("second emit before delay {}", n))
                       .delay(5, TimeUnit.SECONDS)
                       .doOnSuccess(n -> log.info("second emit after delay {}", n)),
                   (n, n2) -> n + n2)
            .doOnSuccess(sum -> log.info("after zip. {}", sum))
            .subscribe();

        return Mono.empty();
    }

    public List<String> filter(List<String> list1, List<String> list2) {
        return list1.stream()
            .filter(str -> list2.stream().anyMatch(str2 -> str2.equals(str)))
            .collect(Collectors.toList());
    }

    @GetMapping("/filterMaybe/test")
    public Mono<String> filterMaybe() {

        List<String> users = Arrays.asList("ab", "cd");
        List<String> usersFromCab = Arrays.asList("ab", "cd");
        List<String> admins = Arrays.asList("admin");

        Single.fromCallable(() -> true)
            .filter(Boolean::booleanValue)
            .flatMap(result -> Flowable.fromIterable(usersFromCab)
                .toList()
                .map(cabList -> filter(users, cabList))
                .toMaybe())
            .defaultIfEmpty(users)
            .doOnSuccess(list -> log.info("{}", list))
            .subscribe();

        // [ab, cd]

        Single.fromCallable(() -> false)
            .filter(Boolean::booleanValue)
            .flatMap(result -> Flowable.fromIterable(usersFromCab)
                .toList()
                .map(cabList -> filter(admins, cabList))
                .toMaybe())
            .defaultIfEmpty(admins)
            .doOnSuccess(list -> log.info("{}", list))
            .subscribe();

        // [admin]

        return Mono.empty();
    }

    @GetMapping("/subscribe/normal")
    public Mono<String> subscribeNormal() {

        log.info("before subscribe");

        Single.fromCallable(() -> "test")
            .doOnSuccess(s -> log.info("success: {}", s))
            .subscribe();

        log.info("after subscribe");

        // [reactor-http-nio-2] before subscribe
        // [reactor-http-nio-2] success: test
        // [reactor-http-nio-2] after subscribe

        return Mono.empty();
    }

    @GetMapping("/subscribe/trampoline")
    public Mono<String> subscribeTrampoline() {

        log.info("before subscribe");

        Single.fromCallable(() -> "test")
            .doOnSuccess(s -> log.info("success: {}", s))
            .subscribeOn(Schedulers.trampoline())
            .subscribe();

        log.info("after subscribe");

        // [reactor-http-nio-2] before subscribe
        // [reactor-http-nio-2] success: test
        // [reactor-http-nio-2] after subscribe

        return Mono.empty();
    }

    @GetMapping("/subscribe/normalDelay")
    public Mono<String> subscribeNormalDelay() {

        log.info("before subscribe");

        Single.fromCallable(() -> "test")
            .delay(5000, TimeUnit.MILLISECONDS)
            .doOnSuccess(s -> log.info("success: {}", s))
            .subscribe();

        log.info("after subscribe");

        // [reactor-http-nio-2] before subscribe
        // [reactor-http-nio-2] after subscribe
        // [RxComputationThreadPool-1] success: test

        return Mono.empty();
    }

    @GetMapping("/subscribe/blockingDelay")
    public Mono<String> subscribeBlocking() {

        log.info("before subscribe");

        Single.fromCallable(() -> "test")
            .delay(5000, TimeUnit.MILLISECONDS, Schedulers.trampoline())
            .doOnSuccess(s -> log.info("success: {}", s))
            .subscribeOn(Schedulers.trampoline())
            .subscribe();

        log.info("after subscribe");

        // [reactor-http-nio-2] before subscribe
        // [reactor-http-nio-2] success: test
        // [reactor-http-nio-2] after subscribe

        return Mono.empty();
    }

    @GetMapping("/subscribe/exception")
    public Mono<String> subscribeException() {

        log.info("before subscribe");

        RuntimeException[] results = new RuntimeException[] { null };

        Single.fromCallable(() -> null)
            .doOnSuccess(s -> log.info("success: {}", s))
            .subscribe(result -> {}, e -> results[0] = new RuntimeException(e));

        try {
            if (Objects.isNull(results[0]) == false) {
                throw results[0];
            }
        } catch (Exception e) {
            log.error("error occur.");
        }

        log.info("after subscribe");

        // [reactor-http-nio-2] before subscribe
        // [reactor-http-nio-2] error occur.
        // [reactor-http-nio-2] after subscribe

        return Mono.empty();
    }
}

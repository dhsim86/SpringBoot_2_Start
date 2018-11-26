package com.dongho.dev.web;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    private final List<String> basket1 = Arrays.asList(new String[] {"kiwi", "orange", "lemon", "orange", "lemon", "kiwi"});
    private final List<String> basket2 = Arrays.asList(new String[] {"banana", "lemon", "lemon", "kiwi"});
    private final List<String> basket3 = Arrays.asList(new String[] {"strawberry", "orange", "lemon", "grape", "strawberry"});

    private final List<List<String>> baskets = Arrays.asList(basket1, basket2, basket3);

    @Getter
    public static class FruitInfo {
        private final List<String> distinctFruits;
        private final Map<String, Long> countFruits;

        public FruitInfo(List<String> distinctFruits, Map<String, Long> countFruits) {
            this.distinctFruits = distinctFruits;
            this.countFruits = countFruits;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            FruitInfo fruitInfo = (FruitInfo)o;

            if (distinctFruits != null ? !distinctFruits.equals(fruitInfo.distinctFruits) : fruitInfo.distinctFruits != null)
                return false;
            return countFruits != null ? countFruits.equals(fruitInfo.countFruits) : fruitInfo.countFruits == null;
        }

        @Override
        public int hashCode() {
            int result = distinctFruits != null ? distinctFruits.hashCode() : 0;
            result = 31 * result + (countFruits != null ? countFruits.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "FruitInfo{" +
                "distinctFruits=" + distinctFruits +
                ", countFruits=" + countFruits +
                '}';
        }

    }

    @GetMapping("/test1")
    public Mono<String> test1() {
        Flux<List<String>> basksetFlux = Flux.fromIterable(baskets);

        basksetFlux.log("basketListFlux").concatMap(basket -> {
            Mono<List<String>> distinctFruits = Flux.fromIterable(basket).log("FruitNameFlux").distinct().collectList();

            Mono<Map<String, Long>> countFruitsMono = Flux.fromIterable(basket).log("FruitCountFlux")
                .groupBy(fruit -> fruit)
                .concatMap(groupedFlux -> groupedFlux.log("GroupedFlux {" + groupedFlux.key() + "}").count()
                    .map(count -> {
                        Map<String, Long> friutCount = new LinkedHashMap<>();
                        friutCount.put(groupedFlux.key(), count);
                        return friutCount;
                    })
                )
                .reduce((accumulatedMap, currentMap) -> new LinkedHashMap<String, Long>() {{
                    putAll(accumulatedMap);
                    putAll(currentMap);
                }});

            return Flux.zip(distinctFruits, countFruitsMono, (distinct, count) -> new FruitInfo(distinct, count));
        }).subscribe(info -> log.info("Info: {}\n", info));

        return Mono.empty();
    }

    @GetMapping("/test2")
    public Mono<String> test2() {
        Flux<List<String>> basksetFlux = Flux.fromIterable(baskets);

        basksetFlux.log("basketListFlux").concatMap(basket -> {
            Mono<List<String>> distinctFruits =
                Flux.fromIterable(basket).log("FruitNameFlux").distinct().collectList().subscribeOn(Schedulers.parallel());

            Mono<Map<String, Long>> countFruitsMono = Flux.fromIterable(basket).log("FruitCountFlux")
                .groupBy(fruit -> fruit)
                .concatMap(groupedFlux -> groupedFlux.log("GroupedFlux {" + groupedFlux.key() + "}").count()
                    .map(count -> {
                        Map<String, Long> friutCount = new LinkedHashMap<>();
                        friutCount.put(groupedFlux.key(), count);
                        return friutCount;
                    })
                )
                .reduce((accumulatedMap, currentMap) -> new LinkedHashMap<String, Long>() {{
                    putAll(accumulatedMap);
                    putAll(currentMap);
                }})
                .subscribeOn(Schedulers.parallel());

            return Flux.zip(distinctFruits, countFruitsMono, (distinct, count) -> new FruitInfo(distinct, count));
        }).subscribe(info -> log.info("Info: {}\n", info));

        return Mono.empty();
    }

    @GetMapping("/test3")
    public Mono<String> test3() {
        Flux<List<String>> basksetFlux = Flux.fromIterable(baskets);

        basksetFlux.log("basketListFlux").concatMap(basket -> {
            Flux<String> source = Flux.fromIterable(basket).log("Hot Basket").publish().autoConnect(2);

            Mono<List<String>> distinctFruits = source.log("FruitNameFlux").distinct().collectList();

            Mono<Map<String, Long>> countFruitsMono = source.log("FruitCountFlux")
                .groupBy(fruit -> fruit)
                .concatMap(groupedFlux -> groupedFlux.log("GroupedFlux {" + groupedFlux.key() + "}").count()
                    .map(count -> {
                        Map<String, Long> friutCount = new LinkedHashMap<>();
                        friutCount.put(groupedFlux.key(), count);
                        return friutCount;
                    })
                )
                .reduce((accumulatedMap, currentMap) -> new LinkedHashMap<String, Long>() {{
                    putAll(accumulatedMap);
                    putAll(currentMap);
                }});

            return Flux.zip(distinctFruits, countFruitsMono, (distinct, count) -> new FruitInfo(distinct, count));
        }).subscribe(info -> log.info("Info: {}\n", info));

        return Mono.empty();
    }

    @GetMapping("/test4")
    public Mono<String> test4() {
        Flux<List<String>> basksetFlux = Flux.fromIterable(baskets);

        basksetFlux.log("basketListFlux").concatMap(basket -> {
            Flux<String> source =
                Flux.fromIterable(basket).log("Hot Basket").publish().autoConnect(2).subscribeOn(Schedulers.newSingle("sub"));

            Mono<List<String>> distinctFruits = source.publishOn(Schedulers.parallel()).log("FruitNameFlux").distinct().collectList();

            Mono<Map<String, Long>> countFruitsMono = source.publishOn(Schedulers.parallel()).log("FruitCountFlux")
                .groupBy(fruit -> fruit)
                .concatMap(groupedFlux -> groupedFlux.log("GroupedFlux {" + groupedFlux.key() + "}").count()
                    .map(count -> {
                        Map<String, Long> friutCount = new LinkedHashMap<>();
                        friutCount.put(groupedFlux.key(), count);
                        return friutCount;
                    })
                )
                .reduce((accumulatedMap, currentMap) -> new LinkedHashMap<String, Long>() {{
                    putAll(accumulatedMap);
                    putAll(currentMap);
                }});

            return Flux.zip(distinctFruits, countFruitsMono, (distinct, count) -> new FruitInfo(distinct, count));
        }).subscribe(info -> log.info("Info: {}\n", info));

        return Mono.empty();
    }

}

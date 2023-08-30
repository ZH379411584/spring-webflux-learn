package com.daxiyan.study;

import org.junit.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * date: 2023/8/28
 * description:
 */
public class TransformFluxTest {
    @Test
    public void map() {
        Flux<String> flux = Flux.just(1)
                .map(String::valueOf);

        StepVerifier.create(flux)
                .expectNext("1")
                .verifyComplete();
    }

    @Test
    public void cast() {
        // 转化为某个类
        Mono<Object> flux = Mono.just(1)
                .cast(Object.class);

        StepVerifier.create(flux)
                .expectNext(1)
                .verifyComplete();
    }


    @Test
    public void flatMap() {

        // flatMap 返回一个 Publisher
        Flux<Integer> flux = Flux.just(1)
                .flatMap(e -> Flux.just(e, e * 2));


        StepVerifier.create(flux)
                .expectNext(1, 2)
                .verifyComplete();
    }

    @Test
    public void handle() {

        Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5)
                .handle((value, sink) -> {
                    if (value % 2 == 0) {
                        sink.next(value);
                    }
                });

        StepVerifier.create(flux)
                .expectNext(2, 4)
                .verifyComplete();
    }

    @Test
    public void flatMapSequential() {
        // flatMapSequential 保证顺序
        Flux<Integer> integerFlux = Flux.range(1, 4)
                .flatMapSequential(e -> {
                    if (e % 2 == 0) {
                        return Mono.just(e).delayElement(Duration.ofMillis(200));
                    } else {
                        return Mono.just(e).delayElement(Duration.ofMillis(100));
                    }
                });


        integerFlux.subscribe(System.out::println);


        integerFlux.blockLast();

        // flatMap 不保证顺序
        Flux<Integer> integerFluxFlatMap = Flux.range(10, 4)
                .flatMap(e -> {
                    if (e % 2 == 0) {
                        return Mono.just(e).delayElement(Duration.ofMillis(100));
                    } else {
                        return Mono.just(e).delayElement(Duration.ofMillis(200));
                    }
                });
        integerFluxFlatMap.subscribe(System.out::println);


        integerFluxFlatMap.blockLast();
    }


    @Test
    public void startWith() {

        Flux<Integer> flux = Flux.just(1).startWith(-1, -2);

        StepVerifier.create(flux)
                .expectNext(-1, -2, 1)
                .verifyComplete();
    }


    @Test
    public void concatWithValues() {
        Flux<Integer> flux = Flux.just(1).concatWithValues(-1, -2);

        StepVerifier.create(flux)
                .expectNext(1, -1, -2)
                .verifyComplete();
    }

    @Test
    public void collectToList() {
        Mono<List<Integer>> mono = Flux.just(1, 2, 3).collectList();

        StepVerifier.create(mono)
                .expectNext(Stream.of(1, 2, 3).collect(Collectors.toList()))
                .verifyComplete();
    }

    @Test
    public void collectToSortedList() {
        Mono<List<Integer>> mono = Flux.just(3, 2, 1).collectSortedList(Integer::compareTo);

        StepVerifier.create(mono)
                .expectNext(Stream.of(1, 2, 3).collect(Collectors.toList()))
                .verifyComplete();
    }

    @Test
    public void collectMap() {
        Mono<Map<Integer, Integer>> mono = Flux.just(3, 2, 1).collectMap((value) -> value);

        Map<Integer, Integer> map = mono.block();

        map.forEach((key, value) -> System.out.println(key + ":" + value));
    }

    @Test
    public void count() {
        Mono<Long> mono = Flux.just(3, 2, 1).count();
        StepVerifier.create(mono)
                .expectNext(3L)
                .verifyComplete();

    }

    @Test
    public void reduce() {
        Mono<Integer> mono = Flux.just(3, 2, 1).reduce(0, (a, b) -> a + b);
        StepVerifier.create(mono)
                .expectNext(6)
                .verifyComplete();
    }


    @Test
    public void scan() {
        Flux<Integer> mono = Flux.just(3, 2, 1).scan((a, b) -> a + b);
        StepVerifier.create(mono)
                .expectNext(3, 5, 6)
                .verifyComplete();

    }

    @Test
    public void all() {
        Mono<Boolean> mono = Flux.just(3, 2, 1).all(e -> e > 0);
        StepVerifier.create(mono)
                .expectNext(true)
                .verifyComplete();

    }


    @Test
    public void concatWith() {
        Flux<Integer> flux = Flux.just(3, 2, 1).concatWith(Flux.just(1, 2, 3));
        StepVerifier.create(flux)
                .expectNext(3, 2, 1, 1, 2, 3)
                .verifyComplete();

    }


    @Test
    public void mergeWith() {
        //concatWith 根据序列的顺序，mergeWith根据生产的顺序
        Flux<Integer> flux = Flux.just(3, 2, 1).delayElements(Duration.ofMillis(200))
                .mergeWith(Flux.just(4, 5, 6).delayElements(Duration.ofMillis(100)));
        flux.subscribe(System.out::println);
        flux.blockLast();

    }

    @Test
    public void zip() {
        Flux<Tuple2<Integer, Integer>> flux = Flux.just(3, 2, 1)
                .zipWith(Flux.just(4, 5, 6));
        flux.subscribe(System.out::println);
        flux.blockLast();

    }

    @Test
    public void expand() throws InterruptedException {

        // 递归生成
        Flux.just(1)
                .expand(num -> {
                    int nextNum = num + 1;
                    if (nextNum <= 5) {
                        return Flux.just(nextNum);
                    } else {
                        return Flux.empty();
                    }
                })
                .subscribe(System.out::println);


    }
    @Test
    public void concatMap()
    {
        Flux.just(5, 10)
                .concatMap(x -> Flux.range(x * 10, 100).take(x))
                .toStream()
                .forEach(System.out::println);
    }


}







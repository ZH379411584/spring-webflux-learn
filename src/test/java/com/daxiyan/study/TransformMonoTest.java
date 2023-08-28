package com.daxiyan.study;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;


/**
 * date: 2023/8/28
 * description:
 */
public class TransformMonoTest {
    @Test
    public void map() {
        Mono<String> mono = Mono.just(1)
                .map(String::valueOf);

        StepVerifier.create(mono)
                .expectNext("1")
                .verifyComplete();
    }

    @Test
    public void cast() {
        // 转化为某个类
        Mono<Object> mono = Mono.just(1)
                .cast(Object.class);

        StepVerifier.create(mono)
                .expectNext(1)
                .verifyComplete();
    }


    @Test
    public void flatMap() {

        // flatMap 返回一个 Publisher
        Mono<Integer> mono = Mono.just(1)
                .flatMap(Mono::just);

        StepVerifier.create(mono)
                .expectNext(1)
                .verifyComplete();
    }

    @Test
    public void handle() {

        // flatMap 返回一个 Publisher
        Mono<Integer> mono = Mono.just(2)
                .handle((value, sink) -> {
                    if (value.equals(2)) {
                        sink.next(value);
                    }
                });

        StepVerifier.create(mono)
                .expectNext(2)
                .verifyComplete();
    }

    @Test
    public void flatMapMany() {

        // flatMap 返回一个 Publisher
        Flux<Integer> mono = Mono.just(2)
                .flatMapMany(e -> Flux.just(e, e + 1));

        StepVerifier.create(mono)
                .expectNext(2, 3)
                .verifyComplete();
    }

    @Test
    public void and() {
        Mono<Void> mono = Mono.defer(() -> {
            System.out.println("mono.just(1)");
            return Mono.just(1);
        }).and(Mono.defer(() -> {
            System.out.println("mono.just(2)");
            return Mono.just(2);
        }));
        mono.subscribe(System.out::println);

    }

    @Test
    public void when() {
        Mono<String> mono1 = Mono.just("Hello");
        Mono<String> mono2 = Mono.just("World");
        Mono<Integer> mono3 = Mono.just(42);

        Mono.when(mono1, mono2, mono3)
                .subscribe();
    }

    @Test
    public void zip() {
        Mono<String> mono1 = Mono.just("Hello");
        Mono<String> mono2 = Mono.just("World");
        Mono<Tuple2<String, String>> tuple2Mono = mono1.zipWith(mono2);
        tuple2Mono.subscribe(System.out::println);
    }


}

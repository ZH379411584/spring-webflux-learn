package com.daxiyan.study;

import org.junit.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreateFluxTest {
    @Test
    public void just() {
        Flux<Integer> integerFlux = Flux.just(1, 2, 3);

        integerFlux.subscribe(System.out::println);

        StepVerifier.create(integerFlux)
                .expectNext(1, 2, 3)
                .verifyComplete();
    }

    @Test
    public void from() {


        // list to flux
        System.out.println("--------------------------");

        List<Integer> numbers = Stream.of(1, 2, 3).collect(Collectors.toList());
        Flux<Integer> integerFlux = Flux.fromIterable(numbers);

        integerFlux.subscribe(System.out::println);

        StepVerifier.create(integerFlux)
                .expectNext(1, 2, 3)
                .verifyComplete();


        // flux to flux
        System.out.println("--------------------------");

        Flux<Integer> integerFlux2 = Flux.from(integerFlux);

        integerFlux2.subscribe(System.out::println);

        StepVerifier.create(integerFlux2)
                .expectNext(1, 2, 3)
                .verifyComplete();


        // stream to flux
        System.out.println("--------------------------");
        Flux<Integer> integerFlux3 = Flux.fromStream(numbers.stream());

        integerFlux3.subscribe(System.out::println);

        // 注意 使用stream 创建的不能多次subscribe
        StepVerifier.create(Flux.fromStream(numbers.stream()))
                .expectNext(1, 2, 3)
                .verifyComplete();

    }

    @Test
    public void range() {

        Flux<Integer> numbers = Flux.range(1, 2);

        numbers.subscribe(System.out::println);

        StepVerifier.create(numbers)
                .expectNext(1, 2)
                .verifyComplete();

    }

    @Test
    public void error() {
        Flux error = Flux.error(new RuntimeException());
        error.subscribe(System.out::println);

        System.out.println("----------------");
        StepVerifier.create(error)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void generate() {
        Flux<String> flux = Flux.generate(
                () -> 0,
                (state, sink) -> {
                    sink.next("3 x " + state + " = " + 3 * state);
                    if (state == 10) {
                        sink.complete();
                    }
                    return state + 1;
                });

        flux.subscribe(System.out::println);
    }

    @Test
    public void create() {
        Flux<String> customFlux = Flux.create(fluxSink -> {
            // 发出元素
            fluxSink.next("Hello");
            fluxSink.next("World");

            // 发出错误信号
            fluxSink.error(new RuntimeException("Custom error"));

            // 发出完成信号
            fluxSink.complete();
        });

        customFlux.subscribe(
                System.out::println,  // 处理元素
                Throwable::printStackTrace,  // 处理错误信号
                () -> System.out.println("Completed")  // 处理完成信号
        );
    }

    @Test
    public void internal() throws InterruptedException {
        Flux<Long> flux = Flux.interval(Duration.ofMillis(200));
        Disposable disposable = flux.log().subscribe();
        Thread.sleep(1000);
        disposable.dispose();


    }
}

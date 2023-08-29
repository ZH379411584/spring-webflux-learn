package com.daxiyan.study;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FilterFluxTest {
    @Test
    public void filter() {
        Flux flux = Flux.just(0, -1, 1, 2)
                .filter(e -> e > 0)
                .log();


        StepVerifier.create(flux)
                .expectNext(1, 2)
                .verifyComplete();
    }

    @Test
    public void filterWhen() {
        Flux flux = Flux.just(0, -1, 1, 2)
                .filterWhen(e -> Mono.just(e > 0));

        StepVerifier.create(flux)
                .expectNext(1, 2)
                .verifyComplete();
    }

    @Test
    public void ofType() {
        StepVerifier.create(
                        Flux.just(1, 2, 3)
                                .ofType(Integer.class)
                                .log())
                .expectNext(1, 2, 3)
                .verifyComplete();

        StepVerifier.create(
                        Flux.just(1, 2, 3)
                                .ofType(Long.class)
                                .log())
                .expectComplete()
                .verify();

    }

    @Test
    public void ignoreElement() {
        // 只关心 Flux 的完成信号或错误信号，而不关心具体的元素
        Mono<Integer> mono = Flux.just(0, -1, 1, 2)
                .filter(e -> e > 0)
                .log()
                .ignoreElements();


        StepVerifier.create(mono)
                .expectComplete()
                .verify();

    }

    @Test
    public void distinct() {
        Flux<Integer> flux = Flux.just(1, 1, 2, 2, 1)
                .distinct()
                .log();


        StepVerifier.create(flux)
                .expectNext(1, 2)
                .verifyComplete();

    }

    @Test
    public void distinctUntilChanged() {
        Flux<Integer> flux = Flux.just(1, 1, 2, 2, 1)
                .distinctUntilChanged()
                .log();


        StepVerifier.create(flux)
                .expectNext(1, 2, 1)
                .verifyComplete();
    }

    @Test
    public void take() {
        Flux<Integer> flux = Flux
                .range(1, 5)
                .take(2)
                .log();

        StepVerifier.create(flux)
                .expectNext(1, 2)
                .verifyComplete();
    }

    @Test
    public void takeLast() {
        Flux<Integer> flux = Flux
                .range(1, 5)
                .takeLast(2)
                .log();

        StepVerifier.create(flux)
                .expectNext(4, 5)
                .verifyComplete();
    }

    @Test
    public void next() {
        Mono<Integer> mono = Flux
                .range(1, 5)
                .next()
                .log();

        StepVerifier.create(mono)
                .expectNext(1)
                .verifyComplete();
    }

    @Test
    public void elementAt() {
        Mono<Integer> mono = Flux
                .range(1, 5)
                .elementAt(2)
                // index 从0开始
                .log();

        StepVerifier.create(mono)
                .expectNext(3)
                .verifyComplete();
    }


    @Test
    public void last() {
        Mono<Integer> mono = Flux
                .range(1, 5)
                .last()
                .log();

        StepVerifier.create(mono)
                .expectNext(5)
                .verifyComplete();
    }


    @Test
    public void skip() {
        Flux<Integer> flux = Flux
                .range(1, 5)
                .skip(2)
                .log();

        StepVerifier.create(flux)
                .expectNext(3, 4, 5)
                .verifyComplete();
    }

    @Test
    public void skipLast() {
        Flux<Integer> flux = Flux
                .range(1, 5)
                .skipLast(2)
                .log();

        StepVerifier.create(flux)
                .expectNext(1, 2, 3)
                .verifyComplete();
    }

    @Test
    public void sample() throws InterruptedException {
        //  每500毫秒，sample()操作符会从源Flux中选择最新的元素发出。
        //  由于源Flux每100毫秒发出一个元素，所以每次采样时，会选择最接近采样时间的元素。
        Flux.interval(Duration.ofMillis(100))
                .sample(Duration.ofMillis(500))
                .take(5)
                .subscribe(System.out::println);

        Thread.sleep(3000);
    }

    @Test
    public void single()  {

        Mono<Integer> mono = Flux.just(1,2,3)
                .single()
                //.doOnError(Throwable::printStackTrace)
                ;

       StepVerifier.create(mono)
               .expectError(IndexOutOfBoundsException.class)
               .verify();



        StepVerifier.create(Flux.just(1).single())
                .expectNext(1)
                .verifyComplete();
    }

}

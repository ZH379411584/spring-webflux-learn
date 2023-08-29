package com.daxiyan.study;

import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FilterMonoTest {

    @Test
    public void filter() {
        Mono mono = Mono.just(1)
                .filter(e -> e > 0)
                .log();


        StepVerifier.create(mono)
                .expectNext(1)
                .verifyComplete();
    }

    @Test
    public void filterWhen() {
        Mono.just(1)
                .filterWhen(e -> Mono.just(e > 0))
                .log()
                .subscribe(System.out::println);
    }

    @Test
    public void ofType() {
        StepVerifier.create(
                        Mono.just(1)
                                .ofType(Integer.class)
                                .log())
                .expectNext(1)
                .verifyComplete();

        StepVerifier.create(
                        Mono.just(1)
                                .ofType(Long.class)
                                .log())
                .expectComplete()
                .verify();

    }
}

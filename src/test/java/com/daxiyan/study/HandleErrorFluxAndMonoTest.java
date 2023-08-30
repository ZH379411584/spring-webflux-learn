package com.daxiyan.study;

import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;
import reactor.core.Disposable;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * date: 2023/8/30
 * description:
 */
public class HandleErrorFluxAndMonoTest {

    @Test
    public void concat() {
        //
        Flux.concat(Flux.error(new RuntimeException()))
                .doOnComplete(() -> System.out.println("Complete"))
                .subscribe();
    }

    @Test
    public void then() {
        Mono.just(1)
                .then(Mono.error(new RuntimeException()))
                .doOnSuccess((e) -> System.out.println("Success:" + e))
                .subscribe();
    }

    @Test
    public void errorSupply() {
        Flux.concat(Flux.error(RuntimeException::new))
                .doOnError(Throwable::printStackTrace)
                .subscribe();
    }

    @Test
    public void onErrorReturn() {
        //
        StepVerifier.create(
                        Flux.error(new RuntimeException())
                                .onErrorReturn(RuntimeException.class, 1))
                .expectNext(1)
                .verifyComplete();
    }

    @Test
    public void onErrorComplete() {
        StepVerifier.create(
                        Flux.just(1, 2)
                                .concatWith(Flux.error(new RuntimeException()))
                                .onErrorComplete())
                .expectNext(1, 2)
                .verifyComplete();
    }

    @Test
    public void onErrorResume() {
        StepVerifier.create(
                        Flux.just(1, 2)
                                .concatWith(Flux.error(new RuntimeException()))
                                .onErrorResume(e -> Mono.just(-1)))
                .expectNext(1, 2, -1)
                .verifyComplete();
    }

    @Test
    public void onErrorMap() {
        StepVerifier.create(
                        Flux.just(1, 2)
                                .concatWith(Flux.error(new Exception()))
                                .onErrorMap(e -> new RuntimeException()))
                .expectNext(1, 2)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void doFinally() {
        StepVerifier.create(
                        Flux.just(1, 2)
                                .concatWith(Flux.error(new RuntimeException()))
                                .doFinally(System.out::println))
                .expectNext(1, 2)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void using() {


        AtomicBoolean isDisposed = new AtomicBoolean();
        Disposable disposableInstance = new Disposable() {
            @Override
            public void dispose() {
                isDisposed.set(true);
            }

            @Override
            public String toString() {
                return "DISPOSABLE";
            }
        };

        Flux<String> flux =
                Flux.using(
                        () -> disposableInstance,
                        disposable -> Flux.just(disposable.toString()),
                        Disposable::dispose
                );

        flux.subscribe();
    }


    @Test
    public void onBackpressureError() throws InterruptedException {

        Flux.range(1, 10)

                //.onBackpressureError()
                .onBackpressureBuffer()
                //.onBackpressureDrop()
                //.onBackpressureLatest()
                .doOnRequest(n -> System.out.println("Requesting " + n + " items"))
                .publishOn(Schedulers.newSingle("single"),1)
                .subscribe(
                        new BaseSubscriber<Integer>(){
                            @Override
                            protected void hookOnSubscribe(Subscription subscription) {
                                subscription.request(1);
                            }

                            @Override
                            protected void hookOnNext(Integer value) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("Consumed value: " + value);
                                request(1);
                            }

                            @Override
                            protected void hookOnError(Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }
                );


        //Thread.sleep(1000);

    }



}





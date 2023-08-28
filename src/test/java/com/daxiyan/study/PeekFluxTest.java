package com.daxiyan.study;

import org.junit.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;


import java.time.Duration;

public class PeekFluxTest {

    @Test
    public void doOnNext() {
        Flux<Integer> flux = Flux.just(1, 2, 3);

        flux.doOnNext(r -> System.out.println("next:" + r)).subscribe(System.out::println);
    }

    @Test
    public void doOnComplete() {
        Flux<Integer> flux = Flux.just(1, 2, 3);

        flux.doOnComplete(() -> System.out.println("complete")).subscribe(System.out::println);
    }

    @Test
    public void doOnError() {
        Flux<Integer> flux = Flux.just(1).concatWith(Flux.error(new RuntimeException()));

        flux.doOnError(RuntimeException.class, e -> System.out.println("error"))
                .subscribe();
    }

    @Test
    public void doOnCancel() throws InterruptedException {
        Flux<Integer> flux = Flux.range(1, 10)
                .delayElements(Duration.ofMillis(300))
                .doOnCancel(() -> System.out.println("cancel"));

        Disposable disposable = flux.subscribe(System.out::println);

        Thread.sleep(1000);

        disposable.dispose();
    }

    @Test
    public void doFirst() {
        Flux<Integer> flux = Flux.just(1, 5)
                .doFirst(() -> System.out.println("first"));

        flux.subscribe(System.out::println);


        Flux.just(100, 101)
                .doFirst(() -> System.out.println("three"))
                .doFirst(() -> System.out.println("two"))
                .doFirst(() -> System.out.println("one"))
                .subscribe(System.out::println);

    }

    @Test
    public void doOnScribe() {
        Flux<Integer> flux = Flux.just(1, 5)
                .doOnSubscribe(s -> s.request(1));

        flux.subscribe(System.out::println);
    }

    @Test
    public void doOnRequest() {
        Flux<Integer> flux = Flux.just(1, 5)
                .doOnSubscribe(s -> s.request(1))
                // 打印出默认请求数量
                .doOnRequest(s -> System.out.println("request:" + s));

        flux.subscribe(System.out::println);
    }


    @Test
    public void doOnTerminate() {
        // 用于在序列终止（完成或发生错误）时执行自定义的操作
        // doOnTerminate() 在序列终止时立即执行操作，包括在错误被传递给订阅者之前
        Flux<Integer> flux = Flux.just(1, 5)
                .doOnTerminate(() -> System.out.println("doOnTerminate"));
        flux.subscribe(System.out::println);

        Flux.just(1, 5).concatWith(Flux.error(new RuntimeException()))
                .doOnTerminate(() -> System.out.println("doOnTerminate"))
                .subscribe(System.out::println);

    }

    @Test
    public void doAfterTerminate() {
        // doAfterTerminate() 在序列终止之后执行操作，包括在错误被传递给订阅者之后
        Flux<Integer> flux = Flux.just(1, 5)
                .doAfterTerminate(() -> System.out.println("doAfterTerminate"));
        flux.subscribe(System.out::println);

        Flux.just(1, 5).concatWith(Flux.error(new RuntimeException()))
                .doAfterTerminate(() -> System.out.println("doAfterTerminate"))
                .subscribe(System.out::println);

    }

    @Test
    public void doOnEach() {
        Flux.just(1, 5)
                .doOnEach(e -> System.out.println("type:" + e.getType() + ",value:" + e.get()))
                .subscribe();

        Flux.just(1, 5).concatWith(Flux.error(new RuntimeException()))
                .doOnEach(e -> System.out.println("type:" + e.getType() + ",value:" + e.get()))
                .subscribe();
    }

    @Test
    public void doFinally() {
        //complete, error, cancel
        Flux.just(1, 5)
                .doFinally(System.out::println)
                .subscribe();

        Flux.just(1, 5).concatWith(Flux.error(new RuntimeException()))
                .doFinally(System.out::println)
                .subscribe();
    }

    @Test
    public void log() {
        Flux.just(1, 2, 3)
                .log()
                .subscribe();

    }

    @Test
    public void materialize() {
        Flux.just(1, 2, 3).materialize()
                .log()
                .subscribe(e -> System.out.println("type:" + e.getType() + ",value:" + e.get()));

    }

    @Test
    public void dematerialize() {
        Flux.just(1, 2, 3)
                // Flux<Signal<Integer>>
                .materialize()
                .dematerialize()
                .log()
                .subscribe();

    }
}

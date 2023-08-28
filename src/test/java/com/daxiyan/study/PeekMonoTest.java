package com.daxiyan.study;

import org.junit.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class PeekMonoTest {

    @Test
    public void doOnNext() {
        Mono<Integer> mono = Mono.just(1);

        mono.doOnNext(System.out::println)
                .subscribe();
    }

    @Test
    public void doOnSuccess() {
        Mono<Integer> mono = Mono.just(1).then(Mono.just(2));

        mono.doOnSuccess(System.out::println)
                .subscribe();
    }

    @Test
    public void doOnError() {
        Mono<Integer> mono = Mono.just(1).then(Mono.error(new RuntimeException()));

        mono.doOnError(RuntimeException.class, Throwable::printStackTrace)
                .subscribe();
    }

    @Test
    public void doOnCancel() throws InterruptedException {
        Mono<Integer> mono = Mono.just(1)
                .delayElement(Duration.ofMillis(300))
                .doOnCancel(() -> System.out.println("cancel"));

        Disposable disposable = mono.subscribe();

        disposable.dispose();
    }

    @Test
    public void doFirst() {
        Mono<Integer> mono = Mono.just(1)
                .doFirst(() -> System.out.println("first"));

        mono.subscribe(System.out::println);


        Mono.just(100)
                .doFirst(() -> System.out.println("three"))
                .doFirst(() -> System.out.println("two"))
                .doFirst(() -> System.out.println("one"))
                .subscribe(System.out::println);

    }

    @Test
    public void doOnScribe() {
        Mono<Integer> mono = Mono.just(1)
                .doOnSubscribe(s -> s.request(1));

        mono.subscribe(System.out::println);
    }

    @Test
    public void doOnRequest() {
        Mono<Integer> mono = Mono.just(1)
                // 打印出默认请求数量
                .doOnRequest(s -> System.out.println("request:" + s));

        mono.subscribe(System.out::println);
    }


    @Test
    public void doOnTerminate() {
        // 用于在序列终止（完成或发生错误）时执行自定义的操作
        // doOnTerminate() 在序列终止时立即执行操作，包括在错误被传递给订阅者之前
        Mono.just(1)
                .doOnTerminate(() -> System.out.println("doOnTerminate"))
                .subscribe(System.out::println);

        Mono.just(1).concatWith(Mono.error(new RuntimeException()))
                .doOnTerminate(() -> System.out.println("doOnTerminate"))
                .subscribe(System.out::println);

    }

    @Test
    public void doAfterTerminate() {
        // doAfterTerminate() 在序列终止之后执行操作，包括在错误被传递给订阅者之后
        Mono.just(1)
                .doAfterTerminate(() -> System.out.println("doOnTerminate"))
                .subscribe(System.out::println);

        Mono.just(1).concatWith(Mono.error(new RuntimeException()))
                .doAfterTerminate(() -> System.out.println("doOnTerminate"))
                .subscribe(System.out::println);

    }

    @Test
    public void doOnEach() {
        Mono.just(1)
                .doOnEach(e -> System.out.println("type:" + e.getType() + ",value:" + e.get()))
                .subscribe();

        Mono.just(1).concatWith(Mono.error(new RuntimeException()))
                .doOnEach(e -> System.out.println("type:" + e.getType() + ",value:" + e.get()))
                .subscribe();
    }

    @Test
    public void doFinally() {
        //complete, error, cancel
        Mono.just(1)
                .doFinally(e -> System.out.println("doFinally:" + e))
                .subscribe();

        Mono.just(1).and(Mono.error(new RuntimeException()))
                .doFinally(e -> System.out.println("doFinally:" + e))
                .subscribe();

        Mono<Integer> mono = Mono.just(1)
                .delayElement(Duration.ofMillis(300))
                .doFinally(e -> System.out.println("doFinally:" + e));

        Disposable disposable = mono.subscribe();

        disposable.dispose();

    }

    @Test
    public void log() {
        Mono.just(1)
                .log()
                .subscribe();

    }

    @Test
    public void materialize() {
        Mono.just(1)
                .materialize()
                .log()
                .subscribe(e -> System.out.println("type:" + e.getType() + ",value:" + e.get()));

    }

    @Test
    public void dematerialize() {
        Mono.just(1)
                .materialize()
                .dematerialize()
                .log()
                .subscribe();

    }
}

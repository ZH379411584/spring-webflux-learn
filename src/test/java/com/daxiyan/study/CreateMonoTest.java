package com.daxiyan.study;

import org.junit.Test;
import reactor.core.publisher.Mono;

public class CreateMonoTest {

    @Test
    public void just() {
        Mono.just("1")
                .subscribe(System.out::println);

        System.out.println("--------------------------");
        Mono.justOrEmpty("1")
                .subscribe(System.out::println);

        System.out.println("--------------------------");
        Mono.never().subscribe(System.out::println);
    }


    @Test
    public void justOrEmpty() {
        Mono.just("1")
                .subscribe(System.out::println);
    }

    @Test
    public void fromSupplier() {
        Mono.fromSupplier(() -> 1)
                .subscribe(System.out::println);
    }

    @Test
    public void fromCaller() {
        Mono.fromCallable(() -> 1)
                .subscribe(System.out::println);
    }

    @Test
    public void emptyAndNever() {
        //Mono.empty() 创建一个立即完成、不发出任何元素的 Mono。 Mono.empty 发出了 OnComplete事件
        Mono.empty().log().subscribe(System.out::println);
        System.out.println("---------------------");
        //Mono.never() 创建一个永远不会完成、不发出任何元素、错误或完成信号的 Mono。
        Mono.never().log().subscribe(System.out::println);
    }

    @Test
    public void error() {
        Mono.error(new RuntimeException()).subscribe(System.out::println);
    }


    public static Mono<Integer> def() {
        return Mono.defer(() -> {
            System.out.println("def ");
            return Mono.empty();
        });
    }

    public static Mono<Integer> defNoDef() {
        System.out.println("defNoDef");
        return Mono.empty();

    }

    @Test
    public void monoDef() {
        // def 包含的代码每次都会调用
        Mono<Integer> monoDef = def();
        monoDef.subscribe(System.out::println);
        monoDef.subscribe(System.out::println);


        Mono<Integer> monoNoDef = defNoDef();
        monoNoDef.subscribe(System.out::println);
        monoNoDef.subscribe(System.out::println);


    }

}

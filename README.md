# spring-webflux-learn
在学习spring cloud gateway的时候想写个API的时候可完全看不懂，原来使用的是spring webFlux 和 reactor 的技术，现在来学习这两项技术。

#  Reactor
## Reactor 是什么？
Reactor is an implementation of the Reactive Programming paradigm, which can be summed up as follows:


> Reactive programming is an asynchronous programming paradigm concerned with data streams and the propagation of change. This means that it becomes possible to express static (e.g. arrays) or dynamic (e.g. event emitters) data streams with ease via the employed programming language(s).
— https://en.wikipedia.org/wiki/Reactive_programming

Reactor 是 Reactor编程范式的一种实现。

Reactor编程范式 是一种异步编程范式，涉及数据流和变化的传播。


## Reactor 解决了什么问题？
当前架构都是一个请求一个线程去处理，此种架构存在如下问题。
1. 线程占据资源，一个计算机能开启的线程数是有限的。
2. 很多时候，线程并没有在调用，线程大部分时间都是在等待IO的读取。
3. 频繁的上下文切换消耗大量CPU的时间，降低系统性能。



解决办法是异步，非阻塞IO。

当前Java实现的方式有两种
1. Callback
2. Future

Callback存在CallbackHell的问题，
Future 虽然有CompletableFuture 提供了较好的API，但是它的API还不够全，不支持 延迟计算，多个值和高级错误处理。



## Reactor Specific

## Reactor API

### Flux

### Mono
 
### Creating a New Sequence

### Transforming an Existing Sequence 

### Peeking into a Sequence（窥视序列）

### Filtering a Sequence

### Handling Errors

### Working with Time

### Splitting a Flux

### Going Back to the Synchronous World

### Multicasting a Flux to several Subscribers

### log

### Test



# 参考资料
- [projectreactor document which-operator 官方文档](https://projectreactor.io/docs/core/release/reference/#which-operator)
- [Hands-On Reactive Programming in Spring 5-Oleh Dokuka Igor Lozynskyi-微信读书](https://weread.qq.com/web/reader/df932ae0722ffcb0df9de61kc8f3245027cc8ffe9a588b8)
- [Build Reactive RESTFUL APIs using Spring Boot/WebFlux Youtube视频](https://www.youtube.com/watch?v=IK26KdGRl48&list=PLnXn1AViWyL70R5GuXt_nIDZytYBnvBdd&index=1)
- [【道法术器】响应式Spring_享学IT的博客-CSDN博客  官方文档翻译者博客](https://blog.csdn.net/get_set/category_9272724.html)
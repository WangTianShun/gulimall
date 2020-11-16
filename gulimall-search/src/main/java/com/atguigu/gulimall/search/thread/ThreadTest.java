package com.atguigu.gulimall.search.thread;

import java.util.concurrent.*;

/**
 * @Description: ThreadTest
 * @Author: WangTianShun
 * @Date: 2020/11/16 8:50
 * @Version 1.0
 */
public class ThreadTest {
    public static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main.............start.......");
//        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("当前运行结果:" + i);
//        }, executor);
        /**
         * 方法成功完成后的感知
         */
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 0;
//            System.out.println("当前运行结果:" + i);
//            return i;
//        }, executor).whenComplete((result,exception)->{
//            //虽然能得到异常信息，但是没法修改返回数据
//            System.out.println("异步任务完成了...结果是"+result+";异常是"+exception);
//        //可以感知异常，同时返回默认值
//        }).exceptionally(throwable -> {
//            return 10;
//        });
        /**
         * 方法执行完成后的处理
         */
//
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 5;
//            System.out.println("当前运行结果:" + i);
//            return i;
//        }, executor).handle((result,exception)->{
//            if (result != null){
//                return result*2;
//            }
//            if (exception != null){
//                return 0;
//            }
//            return 0;
//        });

        /**
         * 线程串行化
         * 1）、thenRun 不能获取得到上一步的执行结果
         */
//        CompletableFuture<Void> thenRunAsync = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 5;
//            System.out.println("当前运行结果:" + i);
//            return i;
//        }, executor).thenRunAsync(() -> {
//            System.out.println("任务2启动了。。。。");
//        }, executor);

        /**
         * 线程串行化
         * 1）、thenRun 不能获取得到上一步的执行结果,无返回值
         * 2)、thenAcceptAsync能接收上一步返回结果，但无返回值
         */
//        CompletableFuture<Void> thenRunAsync = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 5;
//            System.out.println("当前运行结果:" + i);
//            return i;
//        }, executor).thenAcceptAsync(res -> {
//            System.out.println("任务2启动了。。。。"+res);
//        }, executor);

        /**
         * 线程串行化
         * 1）、thenRun 不能获取得到上一步的执行结果,无返回值
         * 2)、thenAcceptAsync能接收上一步返回结果，但无返回值
         * 3)、thenApplyAsync能接收上一步的返回结果，也有返回值
         */
//        CompletableFuture<String> thenApplyAsync = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 5;
//            System.out.println("当前运行结果:" + i);
//            return i;
//        }, executor).thenApplyAsync(res -> {
//            System.out.println("任务2启动了。。。。" + res);
//            return "hello" + res;
//        }, executor);

        /**
         * 两任务组合-都要完成
         */
//        CompletableFuture<Object> future01 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务1线程：" + Thread.currentThread().getId());
//            int i = 10 / 5;
//            System.out.println("任务1线程结束");
//            return i;
//        }, executor);
//        CompletableFuture<Object> future02 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务2线程：" + Thread.currentThread().getId());
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("任务2线程结束");
//            return "hello";
//        }, executor);

        /**
         * 不能得到两个任务的参数，也无返回结果
         */
//        future01.runAfterBothAsync(future02,()->{
//            System.out.println("任务三开始。。。");
//        },executor);
        /**
         * 能得到两个任务的参数，无返回结果
         */
//        future01.thenAcceptBothAsync(future02,(f1,f2)->{
//            System.out.println("任务三开始。。。之前的结果"+f1+":"+f2);
//        },executor);

        /**
         * 能得到两个任务的参数，无返回结果
         */
//        CompletableFuture<String> thenCombineAsync = future01.thenCombineAsync(future02, (f1, f2) -> {
//            System.out.println("任务三开始。。。之前的结果" + f1 + ":" + f2);
//            return f1 + ":" + f2 + "->haha";
//        }, executor);

        /**
         * 两个任务只要有一个完成，我们就执行任务3
         * runAfterEitherAsync  不感知结果，自己也无返回值
         *
         */
//        future01.runAfterEitherAsync(future02,()->{
//            System.out.println("任务三开始。。。");
//        },executor);

        /**
         * 两个任务只要有一个完成，我们就执行任务3
         * runAfterEitherAsync  不感知结果，自己也无返回值
         * acceptEitherAsync   感知结果，自己没有返回值
         */
//        future01.acceptEitherAsync(future02,(res)->{
//            System.out.println("任务三开始。。。"+res);
//        },executor);

        /**
         * 两个任务只要有一个完成，我们就执行任务3
         * runAfterEitherAsync  不感知结果，自己也无返回值
         * acceptEitherAsync   感知结果，自己没有返回值
         */
//        CompletableFuture<String> applyToEitherAsync = future01.applyToEitherAsync(future02, (res) -> {
//            System.out.println("任务三开始。。。" + res);
//            return res.toString() + "-> haha";
//        }, executor);

        CompletableFuture<String> futureAttr = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品的属性");
            return "黑色+256g";
        },executor);

        CompletableFuture<String> futureImg = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("查询商品的图片信息");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello.jpg";
        },executor);

        CompletableFuture<String> futureDesc = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品的介绍");
            return "华为";
        },executor);
//        CompletableFuture<Void> allOf = CompletableFuture.allOf(futureAttr, futureImg, futureDesc);
//        allOf.get();//等待所有线程执行完
        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(futureAttr, futureImg, futureDesc);
        System.out.println("main.............end......."+anyOf.get() );
    }





    public void thread(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main.............start.......");
        /**
         * 1、继承Thread
         *
         * 2、实现Runnable
         *
         * 3、实现Callable接口+FutureTask(可以拿到返回结果，可以处理异常)
         *
         * 4、线程池[ExecutorService]
         *      给线程池直接提交任务
         *      service.execute(new Runnable01());
         *      创建
         *      1）、Executors
         *      2）、new ThreadPoolExecutor();
         *
         *总结：
         *  我们以后再业务代码里面，以上三种启动线程的方式都不用。将所有的多线程异步任务都交给线程池执行
         *
         * 区别：
         *  1、2不能得到返回值。3可以获取返回值
         *  1、2、3都不能控制资源
         *  4可以控制资源，性能稳定。
         */

        //当前系统中池只有一两个，每个异步任务，提交给线程池让他自己去执行就行
        /**
         * 七大参数
         * corePoolSize[5],核心线程数[一直存在，除非（allowCoreThreadTimeOut）]；线程池，创建好以后就准备就绪的线程数量，就等待接收异步任务去执行
         * 5个 Thread thread = new Thread();   thread.start();
         * maximumPoolSize[200]：最大线程数量；控制资源
         * keepAliveTime:存活时间。如果当前正在运行的线程数量大于core数量
         *      释放空闲的线程(maximumPoolSize-corePoolSize)。只要线程空闲大于指定的keepAliveTime;
         * unit:时间单位
         * BlockingQueue<Runnable> workQueue：阻塞队列。如果任务有很多。就会将多的任务放在队列里面
         *      只要有线程空闲，就会去队列里面取出新的任务继续执行
         * ThreadFactory：线程创建的工厂
         * RejectedExecutionHandler handler：如果队列满了，按照我们指定的拒绝策略，拒绝执行
         *
         * 工作顺序
         * 1）、线程池创建，准备好core数量的核心线程，准备接受任务
         * 1.1、core满了，就将再进来的任务放入阻塞队列中。空闲的core就会自己去阻塞队列获取任务执行
         * 1.2、阻塞对垒满了，就直接开新线程执行，最大只能开到max指定的数量
         * 1.3、max满了就用RejectedExecutionHandler拒绝任务
         * 1.4、max都执行完成，有很多空闲，在指定的时间keepAliveTime以后，释放空闲的线程(max-core)。
         *
         *      new LinkedBlockingQueue<>(),默认是Integer的最大值。内存不够
         *
         * 一个线程池 core 7, max 20, queue 50, 100并发进来怎么分配的
         *      7个会立即执行。50个进入队列，在开13个进行执行。剩下30个就是用拒绝策略
         *      如果不想抛弃还要执行，CallerRunsPolicy
         */
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                5,
                200,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
//        Executors.newCachedThreadPool();core是0，所有都可回收
//        Executors.newFixedThreadPool();固定大小，coremax:都不可回收
//        Executors.newScheduledThreadPool();定时任务的线程池
//        Executors.newSingleThreadExecutor();单线程的线程池，后台从队列获取任务，挨个执行
//        1、继承Thread
//        Thread01 thread01 = new Thread01();
//        thread01.start();//启动线程

//        2、实现Runnable
//        Runnable01 runnable01 = new Runnable01();
//        new Thread(runnable01).start();

//        3、实现Callable接口+FutureTask
        FutureTask<Integer> futureTask = new FutureTask<>(new Callable01());
        new Thread(futureTask).start();
        //阻塞等待整个线程执行完成，获取返回结果
        Integer integer = futureTask.get();

//        4、线程池
        executor.execute(new Runnable01());

        System.out.println("main.............end......."+integer);
    }

    public static class Thread01 extends Thread{
        @Override
        public void run() {
            System.out.println("当前线程："+Thread.currentThread().getId());
            int i = 10/2;
            System.out.println("当前运行结果:" + i);
        }
    }

    public static class Runnable01 implements Runnable{

        @Override
        public void run() {
            System.out.println("当前线程："+Thread.currentThread().getId());
            int i = 10/2;
            System.out.println("当前运行结果:" + i);
        }
    }

    public static class Callable01 implements Callable<Integer>{

        @Override
        public Integer call() throws Exception {
            System.out.println("当前线程："+Thread.currentThread().getId());
            int i = 10/2;
            System.out.println("当前运行结果:" + i);
            return i;
        }
    }
}

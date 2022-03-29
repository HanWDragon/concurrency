package com.han.concurrency3;

/**
 * @author Han
 * @date 2022年03月29日 10:43
 */

/**
 * 死锁：线程1等待线程2互斥持有的资源，而线程2也在等待线程1互斥持有的资源，两个线程都无法继续执行
 * 活锁：线程持续重试一个总是失败的操作，导致无法继续执行
 * 饿死：线程一直被调度器延迟访问其赖以执行的资源，也许是调度器先于低优先级的线程而执行高优先级的线程，同时总是会有一个高优先级
 * 的线程可以执行，饿死也叫做无限延迟
 */
public class Test06 {

    private Object lock1 = new Object();
    private Object lock2 = new Object();

    public void myMethod1() {
        try {
            synchronized (lock1) {
                Thread.sleep(100);
                synchronized (lock2) {
                    System.out.println("myMethod1 invoked");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void myMethod2() {
        try {
            synchronized (lock2) {
                Thread.sleep(100);
                synchronized (lock1) {
                    System.out.println("myMethod2 invoked");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Test06 test06 = new Test06();

        Runnable runnable1 = () -> {
            while (true) {
                test06.myMethod1();
            }
        };

        Runnable runnable2 = () -> {
            while (true) {
                test06.myMethod2();
            }
        };

        Thread thread1 = new Thread(runnable1, "myThread1");
        Thread thread2 = new Thread(runnable2, "myThread2");

        thread1.start();
        thread2.start();
    }
}

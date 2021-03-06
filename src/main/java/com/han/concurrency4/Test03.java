package com.han.concurrency4;

/**
 * @author Han
 * @date 2022年03月31日 00:29
 */

/**
 * volatile关键字
 * <p>
 * private volatile int count;
 * <p>
 * volatile关键字主要有三方面作用：
 * <p>
 * 1. 实现long/double类型变量的原子操作（操作系统是32位）
 * 2. 防止指令重排序
 * 3. 实现变量的可见性
 * <p>
 * <p>
 * volatile double a = 1.0
 * <p>
 * 当使用volatile修饰变量时，应用就不会从寄存器中获取该变量的值，而是从内存（高速缓存）中获取。
 * <p>
 * volatile与锁类似的地方有两点：
 * <p>
 * 1. 确保变量的内存可见性
 * 2. 防止指令重排序
 * <p>
 * volatile可以确保对变量写操作的原子性，但不具备排他性
 * 另外的重要一点在于：使用锁可能会导致线程的上下文切换（内核态与用户态之间的切换），但使用volatile并不会出现这种情况
 * 如果要实现volatile写操作的原子性，那么在等号右侧的赋值变量中就不能出现被多线程所共享的变量(也就是这个操作也是原子性多)，哪怕这个变量也是个volatile也不可以。
 * <p>
 * ================ 这些就是错误示范，这样使用不能保证原子性 ===================
 *
 * volatile int a = b + 2;
 * volatile int a = a++;
 * volatile Date date = new Date();
 *
 * ================ 这些就是正确示范 ===================
 *
 * volatile int count = 1;
 * volatile boolean flag = false;
 *
 * <p>
 * 防止指令重排序与实现变量的可见性都是通过一种手段来实现的：内存屏障（memory barrier）
 * <p>
 * int a = 1;
 * String s = "hello";
 * <p>
 * 内存屏障 （Release Barrier，释放屏障）
 * <p>
 * volatile boolean v = false;  // 写入操作
 * <p>
 * 内存屏障 （Store Barrier，存储屏障）
 * <p>
 * <p>
 * Release Barrier：防止下面的volatile与上面的所有操作的指令重排序。
 * Store Barrier：重要作用是刷新处理器缓存，结果是可以确保该存储屏障之前一切的操作所生成的结果对于其他处理器来说都可见。
 * <p>
 * <p>
 * 内存屏障（Load Barrier，加载屏障）
 * <p>
 * boolean v1 = v;
 * <p>
 * 内存屏障（Acquire Barrier，获取屏障）
 * <p>
 * int a = 1;
 * String s = "hello";
 * <p>
 * Load Barrier：可以刷新处理器缓存，同步其他处理器对该volatile变量的修改结果。
 * Acquire Barrier：可以防止上面的volatile读取操作与下面的所有操作语句的指令重排序。
 * <p>
 * <p>
 * 对于volatile关键字变量的读写操作，本质上都是通过内存屏障来执行的。
 * <p>
 * 内存屏障兼具了两方面能力：1. 防止指令重排序， 2. 实现变量内存的可见性。
 * <p>
 * 1. 对于读取操作来说，volatile可以确保该操作与其后续的所有读写操作都不会进行指令重排序。
 * 2. 对于修改操作来说，volatile可以确保该操作与其上面的所有读写操作都不会进行指令重排序。
 * <p>
 * ArrayList
 * <p>
 * volatile与锁的一些比较
 * <p>
 * 锁同样具备变量内存可见性与防止指令重排序的功能。
 * <p>
 * <p>
 * monitorenter
 * 内存屏障（Acquire Barrier，获取屏障）
 * <p>
 * ......
 * <p>
 * <p>
 * 内存屏障 （Release Barrier，释放屏障）
 * monitorexit
 */
public class Test03 {
}

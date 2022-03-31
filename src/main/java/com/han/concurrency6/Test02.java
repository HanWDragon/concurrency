package com.han.concurrency6;

/**
 * @author Han
 * @date 2022年03月31日 21:24
 */

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 对于CAS来说，其操作数主要涉及到如下三个：
 * <p>
 * 1. 需要被操作的内存值V
 * 2. 需要进行比较的值A
 * 3. 需要进行写入的值B
 * <p>
 * 只有当V==A的时候，CAS才会通过原子操作的手段来将V的值更新为B。
 * <p>
 * 关于CAS的限制或是问题：
 * <p>
 * 1. 循环开销问题：并发量大的情况下会导致线程一直自旋
 * 2. 只能保证一个变量的原子操作：可以通过AtomicReference来实现对多个变量的原子操作
 * 3. ABA问题：1 -> 3 -> 1。
 */
public class Test02 {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(5);

        System.out.println(atomicInteger.get());

        System.out.println(atomicInteger.getAndSet(8));

        System.out.println(atomicInteger.get());

        System.out.println(atomicInteger.getAndIncrement());

        System.out.println(atomicInteger.get());
    }
}

package com.han.concurrency4;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * @author Han
 * @date 2022年03月29日 16:24
 */

/**
 * 传统上，我们可以通过synchronized关键字 + wait + notify/notifyAll 来实现多个线程之间的协调与通信，整个过程都是由JVM来帮助
 * 我们实现的；开发者无需（也是无法）了解底层的实现细节
 * <p>
 * 从JDK 5开始，并发包提供了Lock， Condition(await与signal/signalAll)来实现多个线程之间的协调与通信，整个过程都是由开发者来
 * 控制的，而且相比于传统方式，更加灵活，功能也更加强大
 * <p>
 * Thread.sleep与await（或是Object的wait方法）的本质区别：sleep方法本质上不会释放锁，而await会释放锁，并且在signal后，还需要
 * 重新获得锁才能继续执行（该行为与Object的wait方法完全一致）
 */
public class Test02 {
    public static void main(String[] args) {

        BoundedContainer boundedContainer = new BoundedContainer();
        IntStream.range(0, 20).forEach(i -> new Thread(() -> {
            try {
                boundedContainer.put("hello");
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start());

        IntStream.range(0, 20).forEach(i -> new Thread(() -> {
            try {
                boundedContainer.take();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start());
    }
}

class BoundedContainer {

    private final String[] elements = new String[10];

    private final Lock lock = new ReentrantLock();

    private final Condition fullCondition = lock.newCondition();

    private final Condition emptyCondition = lock.newCondition();

    private int elementCount; // elements数组中已有的元素数量

    private int putIndex;

    private int takeIndex;


    public void put(String element) throws InterruptedException {
        this.lock.lock();

        try {
            while (this.elementCount == this.elements.length) {
                emptyCondition.await();
            }

            elements[putIndex] = element;

            if (++putIndex == this.elements.length) {
                putIndex = 0;
            }

            ++elementCount;

            System.out.println("put method: " + Arrays.toString(elements));

            fullCondition.signal();
        } finally {
            this.lock.unlock();
        }
    }

    public String take() throws InterruptedException {
        this.lock.lock();

        try {
            while (0 == this.elementCount) {
                fullCondition.await();
            }

            String element = elements[takeIndex];

            elements[takeIndex] = null;

            if (++takeIndex == this.elements.length) {
                takeIndex = 0;
            }

            --elementCount;

            System.out.println("take method: " + Arrays.toString(elements));

            emptyCondition.signal();

            return element;

        } finally {
            this.lock.unlock();
        }
    }
}

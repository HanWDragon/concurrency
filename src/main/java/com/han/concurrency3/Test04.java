package com.han.concurrency3;

/**
 * @author Han
 * @date 2022年03月28日 23:54
 */
public class Test04 {
    private Object lock = new Object();

    public void method() {
        synchronized (lock) {
            System.out.println("Test");
        }
    }
}

package com.han.concurrency2;

/**
 * @author Han
 * @date 2022年03月27日 13:44
 */
public class ThreadTest {
    public static void main(String[] args) {
        Runnable run = () -> {
            int x = 0;
            while (x != 30) {
                System.out.println("Result -> " + x + " PrintBy -> " + Thread.currentThread().getName());
                x++;
            }
            System.out.println("Result -> " + x);
        };

        new Thread(run).start();
        new Thread(run).start();
    }
}

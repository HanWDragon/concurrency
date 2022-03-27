package com.han.concurrency2;

/**
 * @author Han
 * @date 2022年03月27日 13:44
 */
public class ThreadTest1 {
    static int x = 0;

    public static void main(String[] args) {
        Runnable run1 = () -> {
            try {
                for (int i = 0; i < 1000; i++) {
                    x++;
                    Thread.sleep((long) (Math.random() * 10));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Result -> " + x);
        };

        Runnable run2 = () -> {
            try {
                for (int i = 0; i < 1000; i++) {
                    x--;
                    Thread.sleep((long) (Math.random() * 10));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Result -> " + x);
        };

        new Thread(run1).start();
        new Thread(run2).start();
    }
}

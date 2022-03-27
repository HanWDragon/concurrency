package com.han.concurrency1;

/**
 * @author Han
 * @date 2022年03月27日 12:54
 */
public class MyObject {

    private int counter;

    public synchronized void increase() {
        try {
            while (counter != 0) {
                wait();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        counter++;

        System.out.println(counter);

        notify();
    }

    public synchronized void decrease() {
        try {
            while (counter == 0) {
                wait();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        counter--;

        System.out.println(counter);

        notify();
    }
}


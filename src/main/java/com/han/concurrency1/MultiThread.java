package com.han.concurrency1;

/**
 * @author Han
 * @date 2022年03月27日 12:57
 */
public class MultiThread {

    private MyObject myObject;

    public MultiThread(MyObject myObject) {
        this.myObject = myObject;
    }

    Runnable runIncrease = () -> {
        for (int i = 0; i < 30; i++) {
//            try {
//                Thread.sleep((long) (Math.random() * 1000));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            myObject.increase();
        }
    };

    Runnable runDecrease = () -> {
        for (int i = 0; i < 30; i++) {
//            try {
//                Thread.sleep((long) (Math.random() * 1000));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            myObject.decrease();
        }
    };

    public static void main(String[] args) {
        MyObject object = new MyObject();
        MultiThread test = new MultiThread(object);
        Thread thread1 = new Thread(test.runIncrease);
        Thread thread2 = new Thread(test.runDecrease);
        thread1.start();
        thread2.start();
    }

}

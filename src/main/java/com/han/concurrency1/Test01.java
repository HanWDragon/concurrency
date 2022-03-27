package com.han.concurrency1;

/**
 * @author Han
 * @date 2022年03月27日 11:27
 */
public class Test01 {
    /**
     * 在调用wait方法时，线程必须要持有被调用对象的锁，当调用wait方法后，线程就会释放掉该对象的锁（monitor）。
     * 在调用Thread类的sleep方法时，线程是不会释放掉对象的锁的
     * <p>
     * 关于wait与notify和notifyAll方法的总结：
     * <p>
     * 1. 当调用wait时，首先需要确保调用了wait方法的线程已经持有了对象的锁。
     * 2. 当调用wait后，该线程就会释放掉这个对象的锁，然后进入到等待状态（wait set）
     * 3. 当线程调用了wait后进入到等待状态时，它就可以等待其他线程调用相同对象的notify或notifyAll方法来使得自己被唤醒
     * 4. 一旦这个线程被其他线程唤醒后，该线程就会与其他线程一同开始竞争这个对象的锁（公平竞争）；只有当该线程获取到了这个
     * 对象的锁后，线程才会继续往下执行
     * 5. 调用wait方法的代码片段需要放在一个synchronized块或是synchronized方法中，这样才可以确保线程在调用wait方法前
     * 已经获取到了对象的锁
     * 6. 当调用对象的notify方法时，它会随机唤醒该对象等待集合（wait set）中的任意一个线程，当某个线程被唤醒后，它就会与
     * 其他线程一同竞争对象的锁
     * 7. 当调用对象的notifyAll方法时，它会唤醒该对象等待集合（wait set）中的所有线程，这些线程被唤醒后，又会开始竞争对象的锁。
     * 8. 在某一时刻，只有唯一一个线程可以拥有对象的锁。
     */

    public static void main(String[] args) {
//        try {
//            Object object = new Object();
//            /*
//            Exception in thread "main" java.lang.IllegalMonitorStateException:
//            current thread is not owner at java.base/java.lang.Object.wait(Native Method)
//	        at java.base/java.lang.Object.wait(Object.java:321)
//	        at com.han.concurrency1.Test01.main(Test01.java:12)
//             */
//            object.wait();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        Object object = new Object();
        synchronized (object) {
            try {
                object.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /*
        以下是通过javap 反编译形成的字节码
        public class com.han.concurrency1.Test01 {
  public com.han.concurrency1.Test01();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public static void main(java.lang.String[]);
    Code:
       0: new           #2                  // class java/lang/Object
       3: dup
       4: invokespecial #1                  // Method java/lang/Object."<init>":()V
       7: astore_1
       8: aload_1
       9: dup
      10: astore_2
      11: monitorenter
      12: aload_1
      13: invokevirtual #7                  // Method java/lang/Object.wait:()V
      16: goto          24
      19: astore_3
      20: aload_3
      21: invokevirtual #12                 // Method java/lang/InterruptedException.printStackTrace:()V
      24: aload_2
      25: monitorexit
      26: goto          36
      29: astore        4
      31: aload_2
      32: monitorexit
      33: aload         4
      35: athrow
      36: return
    Exception table:
       from    to  target type
          12    16    19   Class java/lang/InterruptedException
          12    26    29   any
          29    33    29   any
}
         */

    }

}

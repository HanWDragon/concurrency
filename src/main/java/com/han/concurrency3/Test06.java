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

    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

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

    /*
    2022-03-29 11:11:48
Full thread dump Java HotSpot(TM) 64-Bit Server VM (15.0.1+9-18 mixed mode, sharing):

Threads class SMR info:
_java_thread_list=0x00007fe65bc6e670, length=13, elements={
0x00007fe65e02d000, 0x00007fe65e02f800, 0x00007fe65d00f200, 0x00007fe65c097c00,
0x00007fe65c012400, 0x00007fe65c012a00, 0x00007fe65c00fc00, 0x00007fe65d00c400,
0x00007fe65c010200, 0x00007fe65e030400, 0x00007fe65c010800, 0x00007fe65d00e200,
0x00007fe65c010e00
}

"Reference Handler" #2 daemon prio=10 os_prio=31 cpu=0.13ms elapsed=49.01s tid=0x00007fe65e02d000 nid=0x3903 waiting on condition  [0x000070000d576000]
   java.lang.Thread.State: RUNNABLE
        at java.lang.ref.Reference.waitForReferencePendingList(java.base@15.0.1/Native Method)
        at java.lang.ref.Reference.processPendingReferences(java.base@15.0.1/Reference.java:241)
        at java.lang.ref.Reference$ReferenceHandler.run(java.base@15.0.1/Reference.java:213)

"Finalizer" #3 daemon prio=8 os_prio=31 cpu=0.15ms elapsed=49.01s tid=0x00007fe65e02f800 nid=0x3d03 in Object.wait()  [0x000070000d679000]
   java.lang.Thread.State: WAITING (on object monitor)
        at java.lang.Object.wait(java.base@15.0.1/Native Method)
        - waiting on <0x0000000787f02c48> (a java.lang.ref.ReferenceQueue$Lock)
        at java.lang.ref.ReferenceQueue.remove(java.base@15.0.1/ReferenceQueue.java:155)
        - locked <0x0000000787f02c48> (a java.lang.ref.ReferenceQueue$Lock)
        at java.lang.ref.ReferenceQueue.remove(java.base@15.0.1/ReferenceQueue.java:176)
        at java.lang.ref.Finalizer$FinalizerThread.run(java.base@15.0.1/Finalizer.java:170)

"Signal Dispatcher" #4 daemon prio=9 os_prio=31 cpu=0.27ms elapsed=48.99s tid=0x00007fe65d00f200 nid=0xa403 runnable  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Service Thread" #5 daemon prio=9 os_prio=31 cpu=1.79ms elapsed=48.99s tid=0x00007fe65c097c00 nid=0xa303 runnable  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread0" #6 daemon prio=9 os_prio=31 cpu=5.77ms elapsed=48.99s tid=0x00007fe65c012400 nid=0xa103 waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE
   No compile task

"C1 CompilerThread0" #8 daemon prio=9 os_prio=31 cpu=10.93ms elapsed=48.99s tid=0x00007fe65c012a00 nid=0x9f03 waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE
   No compile task

"Sweeper thread" #9 daemon prio=9 os_prio=31 cpu=0.05ms elapsed=48.99s tid=0x00007fe65c00fc00 nid=0x6103 runnable  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Notification Thread" #10 daemon prio=9 os_prio=31 cpu=0.06ms elapsed=48.98s tid=0x00007fe65d00c400 nid=0x6403 runnable  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Common-Cleaner" #11 daemon prio=8 os_prio=31 cpu=0.12ms elapsed=48.98s tid=0x00007fe65c010200 nid=0x9c03 in Object.wait()  [0x000070000df97000]
   java.lang.Thread.State: TIMED_WAITING (on object monitor)
        at java.lang.Object.wait(java.base@15.0.1/Native Method)
        - waiting on <0x0000000787f859b0> (a java.lang.ref.ReferenceQueue$Lock)
        at java.lang.ref.ReferenceQueue.remove(java.base@15.0.1/ReferenceQueue.java:155)
        - locked <0x0000000787f859b0> (a java.lang.ref.ReferenceQueue$Lock)
        at jdk.internal.ref.CleanerImpl.run(java.base@15.0.1/CleanerImpl.java:148)
        at java.lang.Thread.run(java.base@15.0.1/Thread.java:832)
        at jdk.internal.misc.InnocuousThread.run(java.base@15.0.1/InnocuousThread.java:134)

"myThread1" #12 prio=5 os_prio=31 cpu=1.68ms elapsed=48.97s tid=0x00007fe65e030400 nid=0x6903 waiting for monitor entry  [0x000070000e09a000]
   java.lang.Thread.State: BLOCKED (on object monitor)
        at com.han.concurrency3.Test06.myMethod1(Test06.java:24)
        - waiting to lock <0x0000000787f87f08> (a java.lang.Object)
        - locked <0x0000000787f87ef8> (a java.lang.Object)
        at com.han.concurrency3.Test06.lambda$main$0(Test06.java:50)
        at com.han.concurrency3.Test06$$Lambda$1/0x0000000800b8c260.run(Unknown Source)
        at java.lang.Thread.run(java.base@15.0.1/Thread.java:832)

"myThread2" #13 prio=5 os_prio=31 cpu=1.57ms elapsed=48.97s tid=0x00007fe65c010800 nid=0x6b03 waiting for monitor entry  [0x000070000e19d000]
   java.lang.Thread.State: BLOCKED (on object monitor)
        at com.han.concurrency3.Test06.myMethod2(Test06.java:37)
        - waiting to lock <0x0000000787f87ef8> (a java.lang.Object)
        - locked <0x0000000787f87f08> (a java.lang.Object)
        at com.han.concurrency3.Test06.lambda$main$1(Test06.java:56)
        at com.han.concurrency3.Test06$$Lambda$2/0x0000000800b8c488.run(Unknown Source)
        at java.lang.Thread.run(java.base@15.0.1/Thread.java:832)

"DestroyJavaVM" #14 prio=5 os_prio=31 cpu=46.40ms elapsed=48.97s tid=0x00007fe65d00e200 nid=0x2603 waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Attach Listener" #15 daemon prio=9 os_prio=31 cpu=0.72ms elapsed=0.11s tid=0x00007fe65c010e00 nid=0x6c03 waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"VM Thread" os_prio=31 cpu=2.29ms elapsed=49.01s tid=0x00007fe65bc3b5a0 nid=0x4803 runnable

"GC Thread#0" os_prio=31 cpu=0.12ms elapsed=49.02s tid=0x00007fe65bc1a010 nid=0x3203 runnable

"G1 Main Marker" os_prio=31 cpu=0.04ms elapsed=49.02s tid=0x00007fe65bc1b190 nid=0x4d03 runnable

"G1 Conc#0" os_prio=31 cpu=0.02ms elapsed=49.02s tid=0x00007fe65bc1c260 nid=0x3503 runnable

"G1 Refine#0" os_prio=31 cpu=0.04ms elapsed=49.02s tid=0x00007fe65bc37c90 nid=0x4a03 runnable

"G1 Young RemSet Sampling" os_prio=31 cpu=5.79ms elapsed=49.02s tid=0x00007fe65bf106a0 nid=0x3803 runnable

"VM Periodic Task Thread" os_prio=31 cpu=31.06ms elapsed=48.98s tid=0x00007fe65be18350 nid=0x9d03 waiting on condition

JNI global refs: 5, weak refs: 0


Found one Java-level deadlock:
=============================
"myThread1":
  waiting to lock monitor 0x00007fe65d807f00 (object 0x0000000787f87f08, a java.lang.Object),
  which is held by "myThread2"

"myThread2":
  waiting to lock monitor 0x00007fe65d807700 (object 0x0000000787f87ef8, a java.lang.Object),
  which is held by "myThread1"

Java stack information for the threads listed above:
===================================================
"myThread1":
        at com.han.concurrency3.Test06.myMethod1(Test06.java:24)
        - waiting to lock <0x0000000787f87f08> (a java.lang.Object)
        - locked <0x0000000787f87ef8> (a java.lang.Object)
        at com.han.concurrency3.Test06.lambda$main$0(Test06.java:50)
        at com.han.concurrency3.Test06$$Lambda$1/0x0000000800b8c260.run(Unknown Source)
        at java.lang.Thread.run(java.base@15.0.1/Thread.java:832)
"myThread2":
        at com.han.concurrency3.Test06.myMethod2(Test06.java:37)
        - waiting to lock <0x0000000787f87ef8> (a java.lang.Object)
        - locked <0x0000000787f87f08> (a java.lang.Object)
        at com.han.concurrency3.Test06.lambda$main$1(Test06.java:56)
        at com.han.concurrency3.Test06$$Lambda$2/0x0000000800b8c488.run(Unknown Source)
        at java.lang.Thread.run(java.base@15.0.1/Thread.java:832)

Found 1 deadlock.

     */
}

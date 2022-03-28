package com.han.concurrency3;

/**
 * @author Han
 * @date 2022年03月27日 20:04
 */

/**
 * JVM中的同步是基于进入与退出监视器对象（管程对象）（Monitor）来实现的，每个对象实例都会有一个Monitor对象，Monitor对象会和
 * Java对象一同创建并销毁。Monitor对象是由C++来实现的。
 * <p>
 * 当多个线程同时访问一段同步代码时，这些线程会被放到一个EntryList集合中，处于阻塞状态的线程都会被放到该列表当中。接下来，当线程
 * 获取到对象的Monitor时，Monitor是依赖于底层操作系统的mutex lock来实现互斥的，线程获取mutex成功，则会持有该mutex，这时其他
 * 线程就无法再获取到该mutex。
 * <p>
 * 如果线程调用了wait方法，那么该线程就会释放掉所持有的mutex，并且该线程会进入到WaitSet集合（等待集合）中，等待下一次被其他线程
 * 调用notify/notifyAll唤醒。如果当前线程顺利执行完毕方法，那么它也会释放掉所持有的mutex。
 * <p>
 * 总结一下：同步锁在这种实现方式当中，因为Monitor是依赖于底层的操作系统实现，这样就存在用户态与内核态之间的切换，所以会增加性能开销。
 * <p>
 * 通过对象互斥锁的概念来保证共享数据操作的完整性。每个对象都对应于一个可称为『互斥锁』的标记，这个标记用于保证在任何时刻，只能有一个
 * 线程访问该对象。
 * <p>
 * 那些处于EntryList与WaitSet中的线程均处于阻塞状态，阻塞操作是由操作系统来完成的，在linux下是通过pthread_mutex_lock函数实现的。
 * 线程被阻塞后便会进入到内核调度状态，这会导致系统在用户态与内核态之间来回切换，严重影响锁的性能。
 * <p>
 * 解决上述问题的办法便是自旋（Spin）。其原理是：当发生对Monitor的争用时，若Owner能够在很短的时间内释放掉锁，则那些正在争用的线程就可以稍微
 * 等待一下（即所谓的自旋），在Owner线程释放锁之后，争用线程可能会立刻获取到锁，从而避免了系统阻塞。不过，当Owner运行的时间超过了临界值
 * 后，争用线程自旋一段时间后依然无法获取到锁，这时争用线程则会停止自旋而进入到阻塞状态。所以总体的思想是：先自旋，不成功再进行阻塞，尽量
 * 降低阻塞的可能性，这对那些执行时间很短的代码块来说有极大的性能提升。显然，自旋在多处理器（多核心）上才有意义。
 * <p>
 * 互斥锁的属性：
 * <p>
 * 1. PTHREAD_MUTEX_TIMED_NP：这是缺省值，也就是普通锁。当一个线程加锁以后，其余请求锁的线程将会形成一个等待队列，并且在解锁后按照优先级
 * 获取到锁。这种策略可以确保资源分配的公平性。
 * 2. PTHREAD_MUTEX_RECURSIVE_NP：嵌套锁。允许一个线程对同一个锁成功获取多次，并通过unlock解锁。如果是不同线程请求，则在加锁线程解锁时
 * 重新进行竞争。
 * 3. PTHREAD_MUTEX_ERRORCHECK_NP：检错锁。如果一个线程请求同一个锁，则返回EDEADLK，否则与PTHREAD_MUTEX_TIMED_NP类型动作相同，这样
 * 就保证了当不允许多次加锁时不会出现最简单情况下的死锁。
 * 4. PTHREAD_MUTEX_ADAPTIVE_NP：适应锁，动作最简单的锁类型，仅仅等待解锁后重新竞争。
 * <p>
 * ======================
 *   https://openjdk.java.net/
 *   这个就是构造器函数，我们可以看到这个类中有哪些元素，我们可以找到对应的源码进行了解。
 *   // initialize the monitor, exception the semaphore, all other fields
 *   // are simple integers or pointers
 *   ObjectMonitor() {
 *     _header       = NULL;
 *     _count        = 0;
 *     _waiters      = 0,
 *     _recursions   = 0;
 *     _object       = NULL;
 *     _owner        = NULL;
 *     _WaitSet      = NULL; <- 这个就是我们说的等待队列
 *     _WaitSetLock  = 0 ;
 *     _Responsible  = NULL ;
 *     _succ         = NULL ;
 *     _cxq          = NULL ;
 *     FreeNext      = NULL ;
 *     _EntryList    = NULL ;
 *     _SpinFreq     = 0 ;
 *     _SpinClock    = 0 ;
 *     OwnerIsThread = 0 ;
 *     _previous_owner_tid = 0;
 *   }
 * ======================
 * <p>
 * 在JDK 1.5之前，我们若想实现线程同步，只能通过synchronized关键字这一种方式来达成；底层，Java也是通过synchronized关键字来做到数据的
 * 原子性维护的；synchronized关键字是JVM实现的一种内置锁，从底层角度来说，这种锁的获取与释放都是由JVM帮助我们隐式实现的。
 * <p>
 * 从JDK 1.5开始，并发包引入了Lock锁，Lock同步锁是基于Java来实现的，因此锁的获取与释放都是通过Java代码来实现与控制的；然而，synchronized
 * 是基于底层操作系统的Mutex Lock来实现的，每次对锁的获取与释放动作都会带来用户态与内核态之间的切换，这种切换会极大地增加系统的负担；在并发量
 * 较高时，也就是说锁的竞争比较激烈时，synchronized锁在性能上的表现就非常差。
 * <p>
 * 从JDK 1.6开始，synchronized锁的实现发生了很大的变化；JVM引入了相应的优化手段来提升synchronized锁的性能，这种提升涉及到偏向锁、轻量级锁
 * 及重量级锁等，从而减少锁的竞争所带来的用户态与内核态之间的切换；这种锁的优化实际上是通过Java对象头中的一些标志位来去实现的；对于锁的访问与改变，
 * 实际上都与Java对象头息息相关。
 * <p>
 * 从JDK 1.6开始，对象实例在堆当中会被划分为三个组成部分：对象头、实例数据与对齐填充。
 * <p>
 * 对象头主要也是由3块内容来构成：
 * <p>
 * 1. Mark Word
 * 2. 指向类的指针
 * 3. 数组长度
 * <p>
 * 其中Mark Word（它记录了对象、锁及垃圾回收相关的信息，在64位的JVM中，其长度也是64bit）的位信息包括了如下组成部分：
 * <p>
 * 1. 无锁标记
 * 2. 偏向锁标记
 * 3. 轻量级锁标记
 * 4. 重量级锁标记
 * 5. GC标记
 * <p>
 * 对于synchronized锁来说，锁的升级主要都是通过Mark Word中的锁标志位与是否是偏向锁标志位来达成的；synchronized关键字所对应的锁都是先从偏向锁
 * 开始，随着锁竞争的不断升级，逐步演化至轻量级锁，最后则变成了重量级锁。
 * <p>
 * 对于锁的演化来说，它会经历如下阶段：
 * <p>
 * 无锁 -> 偏向锁 -> 轻量级锁 -> 重量级锁
 * <p>
 * 偏向锁：
 * <p>
 * 针对于一个线程来说的，它的主要作用就是优化同一个线程多次获取一个锁的情况；如果一个synchronized方法被一个线程访问，那么这个方法所在的对象
 * 就会在其Mark Word中将偏向锁进行标记，同时还会有一个字段来存储该线程的ID；当这个线程再次访问同一个synchronized方法时，它会检查这个对象
 * 的Mark Word的偏向锁标记以及是否指向了其线程ID，如果是的话，那么该线程就无需再去进入管程（Monitor）了，而是直接进入到该方法体中。
 * <p>
 * 如果是另外一个线程访问这个synchronized方法，那么实际情况会如何呢？
 * <p>
 * 偏向锁会被取消掉。
 * <p>
 * <p>
 * 轻量级锁：
 * <p>
 * 若第一个线程已经获取到了当前对象的锁，这时第二个线程又开始尝试争抢该对象的锁，由于该对象的锁已经被第一个线程获取到，因此它是偏向锁，而第二个线程
 * 在争抢时，会发现该对象头中的Mark Word已经是偏向锁，但里面存储的线程ID并不是自己（是第一个线程），那么它会进行CAS（Compare and Swap），从而
 * 获取到锁，这里面存在两种情况：
 * <p>
 * 1. 获取锁成功：那么它会直接将Mark Word中的线程ID由第一个线程变成自己（偏向锁标记位保持不变），这样该对象依然会保持偏向锁的状态。
 * 2. 获取锁失败：则表示这时可能会有多个线程同时在尝试争抢该对象的锁，那么这时偏向锁就会进行升级，升级为轻量级锁
 * <p>
 * <p>
 * 自旋锁：
 * <p>
 * 若自旋失败（依然无法获取到锁），那么锁就会转化为重量级锁，在这种情况下，无法获取到锁的线程都会进入到Monitor（即内核态）
 * <p>
 * 自旋最大的一个特点就是避免了线程从用户态进入到内核态。
 * <p>
 * <p>
 * 重量级锁：
 * <p>
 * 线程最终从用户态进入到了内核态。
 */
public class Test03 {
    public static synchronized void method() {
        System.out.println("hello world");
    }
    /*
Classfile /Users/hwl/IdeaProjects/concurrency/build/classes/java/main/com/han/concurrency3/Test03.class
  Last modified 2022年3月27日; size 493 bytes
  SHA-256 checksum 8f68678739717cd7254f5f7291469550fcdb9c8387d28e8bb45d53fad93c19ad
  Compiled from "Test03.java"
public class com.han.concurrency3.Test03
  minor version: 0
  major version: 59
  flags: (0x0021) ACC_PUBLIC, ACC_SUPER
  this_class: #21                         // com/han/concurrency3/Test03
  super_class: #2                         // java/lang/Object
  interfaces: 0, fields: 0, methods: 2, attributes: 1
Constant pool:
   #1 = Methodref          #2.#3          // java/lang/Object."<init>":()V
   #2 = Class              #4             // java/lang/Object
   #3 = NameAndType        #5:#6          // "<init>":()V
   #4 = Utf8               java/lang/Object
   #5 = Utf8               <init>
   #6 = Utf8               ()V
   #7 = Fieldref           #8.#9          // java/lang/System.out:Ljava/io/PrintStream;
   #8 = Class              #10            // java/lang/System
   #9 = NameAndType        #11:#12        // out:Ljava/io/PrintStream;
  #10 = Utf8               java/lang/System
  #11 = Utf8               out
  #12 = Utf8               Ljava/io/PrintStream;
  #13 = String             #14            // hello world
  #14 = Utf8               hello world
  #15 = Methodref          #16.#17        // java/io/PrintStream.println:(Ljava/lang/String;)V
  #16 = Class              #18            // java/io/PrintStream
  #17 = NameAndType        #19:#20        // println:(Ljava/lang/String;)V
  #18 = Utf8               java/io/PrintStream
  #19 = Utf8               println
  #20 = Utf8               (Ljava/lang/String;)V
  #21 = Class              #22            // com/han/concurrency3/Test03
  #22 = Utf8               com/han/concurrency3/Test03
  #23 = Utf8               Code
  #24 = Utf8               LineNumberTable
  #25 = Utf8               LocalVariableTable
  #26 = Utf8               this
  #27 = Utf8               Lcom/han/concurrency3/Test03;
  #28 = Utf8               method
  #29 = Utf8               SourceFile
  #30 = Utf8               Test03.java
{
  public com.han.concurrency3.Test03();
    descriptor: ()V
    flags: (0x0001) ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 110: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   Lcom/han/concurrency3/Test03;

  public static synchronized void method();
    descriptor: ()V
    flags: (0x0029) ACC_PUBLIC, ACC_STATIC, ACC_SYNCHRONIZED <- 这里就是证明
    Code:
      stack=2, locals=0, args_size=0
         0: getstatic     #7                  // Field java/lang/System.out:Ljava/io/PrintStream;
         3: ldc           #13                 // String hello world
         5: invokevirtual #15                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
         8: return
      LineNumberTable:
        line 112: 0
        line 113: 8
}
SourceFile: "Test03.java"
     */
}

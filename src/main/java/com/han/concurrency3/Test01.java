package com.han.concurrency3;

/**
 * @author Han
 * @date 2022年03月27日 14:59
 */

/**
 * 当我们使用synchronized关键字来修饰代码块时，字节码层面上是通过monitorenter与monitorexit指令来实现的锁的获取与释放动作。
 * 当线程进入到monitorenter指令后，线程将会持有Monitor对象，退出monitorenter指令后，线程将会释放Monitor对象
 */

public class Test01 {
    private Object lock = new Object();


    public void method() {
        synchronized (lock) {
            System.out.println("Hello");
        }
    }
    /*
    Classfile /Users/hwl/IdeaProjects/concurrency/build/classes/java/main/com/han/concurrency3/Test1.class
  Last modified 2022年3月27日; size 672 bytes
  SHA-256 checksum 2eddea41a8916453bb6f11216872255ae3da714b932232abb3c1d57715f25a7e
  Compiled from "Test1.java"
public class com.han.concurrency3.Test1
  minor version: 0
  major version: 59
  flags: (0x0021) ACC_PUBLIC, ACC_SUPER
  this_class: #8                          // com/han/concurrency3/Test1
  super_class: #2                         // java/lang/Object
  interfaces: 0, fields: 1, methods: 2, attributes: 1
Constant pool:
   #1 = Methodref          #2.#3          // java/lang/Object."<init>":()V
   #2 = Class              #4             // java/lang/Object
   #3 = NameAndType        #5:#6          // "<init>":()V
   #4 = Utf8               java/lang/Object
   #5 = Utf8               <init>
   #6 = Utf8               ()V
   #7 = Fieldref           #8.#9          // com/han/concurrency3/Test1.lock:Ljava/lang/Object;
   #8 = Class              #10            // com/han/concurrency3/Test1
   #9 = NameAndType        #11:#12        // lock:Ljava/lang/Object;
  #10 = Utf8               com/han/concurrency3/Test1
  #11 = Utf8               lock
  #12 = Utf8               Ljava/lang/Object;
  #13 = Fieldref           #14.#15        // java/lang/System.out:Ljava/io/PrintStream;
  #14 = Class              #16            // java/lang/System
  #15 = NameAndType        #17:#18        // out:Ljava/io/PrintStream;
  #16 = Utf8               java/lang/System
  #17 = Utf8               out
  #18 = Utf8               Ljava/io/PrintStream;
  #19 = String             #20            // Hello
  #20 = Utf8               Hello
  #21 = Methodref          #22.#23        // java/io/PrintStream.println:(Ljava/lang/String;)V
  #22 = Class              #24            // java/io/PrintStream
  #23 = NameAndType        #25:#26        // println:(Ljava/lang/String;)V
  #24 = Utf8               java/io/PrintStream
  #25 = Utf8               println
  #26 = Utf8               (Ljava/lang/String;)V
  #27 = Utf8               Code
  #28 = Utf8               LineNumberTable
  #29 = Utf8               LocalVariableTable
  #30 = Utf8               this
  #31 = Utf8               Lcom/han/concurrency3/Test1;
  #32 = Utf8               method
  #33 = Utf8               StackMapTable
  #34 = Class              #35            // java/lang/Throwable
  #35 = Utf8               java/lang/Throwable
  #36 = Utf8               SourceFile
  #37 = Utf8               Test1.java
{
  public com.han.concurrency3.Test1();
    descriptor: ()V
    flags: (0x0001) ACC_PUBLIC
    Code:
      stack=3, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: aload_0
         5: new           #2                  // class java/lang/Object
         8: dup
         9: invokespecial #1                  // Method java/lang/Object."<init>":()V
        12: putfield      #7                  // Field lock:Ljava/lang/Object;
        15: return
      LineNumberTable:
        line 7: 0
        line 8: 4
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      16     0  this   Lcom/han/concurrency3/Test1;

  public void method();
    descriptor: ()V
    flags: (0x0001) ACC_PUBLIC
    Code:
      stack=2, locals=3, args_size=1
         0: aload_0
         1: getfield      #7                  // Field lock:Ljava/lang/Object;
         4: dup
         5: astore_1
         6: monitorenter
         7: getstatic     #13                 // Field java/lang/System.out:Ljava/io/PrintStream;
        10: ldc           #19                 // String Hello
        12: invokevirtual #21                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
        15: aload_1
        16: monitorexit
        17: goto          25
        20: astore_2
        21: aload_1
        22: monitorexit
        23: aload_2
        24: athrow
        25: return
      Exception table:
         from    to  target type
             7    17    20   any
            20    23    20   any
      LineNumberTable:
        line 12: 0
        line 13: 7
        line 14: 15
        line 15: 25
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      26     0  this   Lcom/han/concurrency3/Test1;
      StackMapTable: number_of_entries = 2
        frame_type = 255 /* full_frame
    offset_delta = 20
    locals = [ class com/han/concurrency3/Test1, class java/lang/Object ]
    stack = [ class java/lang/Throwable ]
    frame_type = 250 /* chop
    offset_delta = 4

        */
}

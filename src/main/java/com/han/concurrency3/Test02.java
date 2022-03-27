package com.han.concurrency3;

/**
 * @author Han
 * @date 2022年03月27日 18:28
 */

/**
 * 对于synchronized关键字修饰方法来说，并没有出现monitorenter与monitorexit指令，而是出现了一个ACC_SYNCHRONIZED标志。
 * JVM使用了ACC_SYNCHRONIZED访问标志来区分一个方法是否为同步方法；当方法被调用时，调用指令会检查该方法是否拥有ACC_SYNCHRONIZED标志，
 * 如果有，那么执行线程将会先持有方法所在对象的Monitor对象，然后再去执行方法体；在该方法执行期间，其他任何线程均无法再获取到这个Monitor对象，
 * 当线程执行完该方法后，它会释放掉这个Monitor对象。
 */
public class Test02 {
    public synchronized void method() {
        System.out.println("hello world");
    }
    /*
  Classfile /Users/hwl/IdeaProjects/concurrency/build/classes/java/main/com/han/concurrency3/Test02.class
  Last modified 2022年3月27日; size 511 bytes
  SHA-256 checksum e339c6dc158f4ed809ea8f6950b9d7b2d4cba79ecd234a420a5e051dbae5b61b
  Compiled from "Test02.java"
public class com.han.concurrency3.Test02
  minor version: 0
  major version: 59
  flags: (0x0021) ACC_PUBLIC, ACC_SUPER
  this_class: #21                         // com/han/concurrency3/Test02
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
  #21 = Class              #22            // com/han/concurrency3/Test02
  #22 = Utf8               com/han/concurrency3/Test02
  #23 = Utf8               Code
  #24 = Utf8               LineNumberTable
  #25 = Utf8               LocalVariableTable
  #26 = Utf8               this
  #27 = Utf8               Lcom/han/concurrency3/Test02;
  #28 = Utf8               method
  #29 = Utf8               SourceFile
  #30 = Utf8               Test02.java
{
  public com.han.concurrency3.Test02();
    descriptor: ()V
    flags: (0x0001) ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 15: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   Lcom/han/concurrency3/Test02;

  public synchronized void method();
    descriptor: ()V
    flags: (0x0021) ACC_PUBLIC, ACC_SYNCHRONIZED <- 这就就是标志所在的地方
    Code:
      stack=2, locals=1, args_size=1
         0: getstatic     #7                  // Field java/lang/System.out:Ljava/io/PrintStream;
         3: ldc           #13                 // String hello world
         5: invokevirtual #15                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
         8: return
      LineNumberTable:
        line 17: 0
        line 18: 8
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       9     0  this   Lcom/han/concurrency3/Test02;
}
SourceFile: "Test02.java"
     */
}

package com.han.concurrency6;

/**
 * @author Han
 * @date 2022年03月31日 21:24
 */

/**
 * CAS
 * <p>
 * 1. synchronized关键字与Lock等锁机制都是悲观锁：无论做何种操作，首先都需要先上锁，接下来再去执行后续操作，从而确保了
 * 接下来的所有操作都是由当前这个线程来执行的。
 * 2. 乐观锁：线程在操作之前不会做任何预先的处理，而是直接去执行；当在最后执行变量更新的时候，当前线程需要有一种机制来确保
 * 当前被操作的变量是没有被其他线程修改的；CAS是乐观锁的一种极为重要的实现方式。
 * <p>
 * CAS (Compare And Swap)
 * <p>
 * 比较与交换：这是一个不断循环的过程，一直到变量值被修改成功为止。CAS本身是由硬件指令来提供支持的，换句话说，硬件中是通过一个
 * 原子指令来实现比较与交换的；因此，CAS可以确保变量操作的原子性的。
 */
public class Test01 {

    private int count;

    public synchronized int getCount() {
        return count;
    }

//  读取 -> 修改 -> 写入：这三个操作并非原子操作

    public synchronized void increaseCount() {
        ++this.count;
    }

    /*
    Classfile /Users/hwl/IdeaProjects/concurrency/build/classes/java/main/com/han/concurrency6/Test01.class
  Last modified 2022年3月31日; size 480 bytes
  SHA-256 checksum f035861670743b9e27c98f34804b0558569500b4175759cd46f0368bd8246663
  Compiled from "Test01.java"
public class com.han.concurrency6.Test01
  minor version: 0
  major version: 59
  flags: (0x0021) ACC_PUBLIC, ACC_SUPER
  this_class: #8                          // com/han/concurrency6/Test01
  super_class: #2                         // java/lang/Object
  interfaces: 0, fields: 1, methods: 3, attributes: 1
Constant pool:
   #1 = Methodref          #2.#3          // java/lang/Object."<init>":()V
   #2 = Class              #4             // java/lang/Object
   #3 = NameAndType        #5:#6          // "<init>":()V
   #4 = Utf8               java/lang/Object
   #5 = Utf8               <init>
   #6 = Utf8               ()V
   #7 = Fieldref           #8.#9          // com/han/concurrency6/Test01.count:I
   #8 = Class              #10            // com/han/concurrency6/Test01
   #9 = NameAndType        #11:#12        // count:I
  #10 = Utf8               com/han/concurrency6/Test01
  #11 = Utf8               count
  #12 = Utf8               I
  #13 = Utf8               Code
  #14 = Utf8               LineNumberTable
  #15 = Utf8               LocalVariableTable
  #16 = Utf8               this
  #17 = Utf8               Lcom/han/concurrency6/Test01;
  #18 = Utf8               getCount
  #19 = Utf8               ()I
  #20 = Utf8               increaseCount
  #21 = Utf8               SourceFile
  #22 = Utf8               Test01.java
{
  public com.han.concurrency6.Test01();
    descriptor: ()V
    flags: (0x0001) ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 21: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   Lcom/han/concurrency6/Test01;

  public synchronized int getCount();
    descriptor: ()I
    flags: (0x0021) ACC_PUBLIC, ACC_SYNCHRONIZED
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: getfield      #7                  // Field count:I
         4: ireturn
      LineNumberTable:
        line 26: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   Lcom/han/concurrency6/Test01;

  public synchronized void increaseCount();
    descriptor: ()V
    flags: (0x0021) ACC_PUBLIC, ACC_SYNCHRONIZED
    Code:
      stack=3, locals=1, args_size=1
         0: aload_0
         1: dup
         2: getfield      #7                  // Field count:I
         5: iconst_1
         6: iadd
         7: putfield      #7                  // Field count:I
        10: return
      LineNumberTable:
        line 32: 0
        line 33: 10
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      11     0  this   Lcom/han/concurrency6/Test01;
}
SourceFile: "Test01.java"

     */
}

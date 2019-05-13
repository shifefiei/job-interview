### synchronized与Lock的区别

- synchronized是java内置关键字，
    1. 用于修饰方法时获取的锁是当前调用方法的对象
    2. 当synchronized()修饰的同步代码块时，synchronized拿到的是指定对象的锁
    3. 当synchronized修饰的静态方法时，synchronized 获取的锁是Class对象
    
    
- Lock是是一个接口，定义了一系列的锁操作方式，依赖于 AQS 实现锁的获取和释放。
    1. 它提供了读写锁、公平锁和非公平锁　，比如 ReentrantLock 、ReentrantReadWriteLock 就是Lock的实现类。
    2. 一般使用在代码块之上，手动的获取锁和释放锁。

- 区别：
1. 由于Lock接口是基于JDK层面的，锁的释放动作必须手动进行。 synchronized 属于JVM层面，锁的获取和释放动作都由JVM自动进行。



### synchronized 与 volatile 的区别

1. volatile 关键字
- 保证了不同线程对这个变量进行操作时的可见性，即一个线程修改了某个变量的值，这新值对其他线程来说是立即可见的

- 禁止进行指令重排序

- volatile 保证了变量的可见性，但并不能并不能保证其原子性操作
 
- volatile 本质是在告诉jvm 当前变量在寄存器（工作内存）中的值是不确定的，需要从主存中读取；synchronized 则是锁定当前变量，只有当前线程可以访问该变量，其他线程被阻塞住；

- volatile 仅能使用在变量级别；synchronized 修饰方法、代码块之上；

- volatile 仅能实现变量的修改可见性，并不能保证原子性；synchronized 则可以保证变量的修改可见性和原子性；

- volatile 不会造成线程的阻塞；synchronized 可能会造成线程的阻塞；

### reentrantLock 与 synchronized

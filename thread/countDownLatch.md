### [CAS操作](https://segmentfault.com/a/1190000013660507)

### [AQS 是什么](https://segmentfault.com/a/1190000014102756)
AQS 是 AbstractQueuedSynchronizer 的简称。
- AQS是一个用来构建锁和同步器的框架，比如我们提到的ReentrantLock,CountDownLatch；它仅仅只是提供独占锁和共享锁两种方式；
AQS中采用了一个state的状态位 + 一个FIFO的队列的方式，记录了锁的获取，释放等

[AQS理解](https://www.jianshu.com/p/fe027772e156)<br/>

[AbstractQueuedSynchronizer](https://blog.csdn.net/L_BestCoder/article/details/79306039)<br/>
[ReentrantLock](https://blog.csdn.net/jeffleo/article/details/56677425)<br/>
[并发编程网 AbstractQueuedSynchronizer](http://ifeve.com/introduce-abstractqueuedsynchronizer/)<br/>
[AbstractQueuedSynchronizer](https://blog.csdn.net/jeffleo/article/details/56677425)




### CountDownLatch

CountDownLatch通过AQS里面的共享锁来实现的，
在创建CountDownLatch时候，会传递一个参数count，该参数是锁计数器的初始状态，表示该共享锁能够被count个线程同时获取。
当某个线程调用CountDownLatch对象的await方法时候，该线程会等待共享锁可获取时，才能获取共享锁继续运行，
而共享锁可获取的的条件是state == 0，而锁倒数计数器的初始值为count，每当一个线程调用该CountDownLatch对象的countDown()方法时候，
计数器才-1，所以必须有count个线程调用该countDown()方法后，锁计数器才为0，这个时候等待的线程才能继续运行。

[分析CountDownLatch的实现原理](https://www.jianshu.com/p/7c7a5df5bda6?ref=myread)<br/>
https://cloud.tencent.com/developer/article/1038486






# 线程池
1. 线程资源必须通过线程池提供，不允许应用中自行显示的创建。使用线程池的好处是减少在创建和销毁线程上所花的时间以及系统
资源的开销。
2. 线程池的核心思想是把宝贵的线程资源放到一个池子中，每次使用的时候就去池子中取，用完之后又放回去。


# 常见的线程池
1. Executors.newSingleThreadExecutor()
创建单个线程，池子里只有一个线程，如果线程因为异常结束，会新建一个线程，使用 LinkedBlockingQueue 队列。

2. Executors.newCachedThreadPool()
创建一个可伸缩的线程池，将复用可用线程，如果没有可用线程，则创建一个添加到池子中去，60s中未使用的线程将被移除，
使用 SynchronousQueue 队列。

3. Executors.newFixedThreadPool(int nThread)
创建固定大小的线程池，使用的是 LinkedBlockingQueue 队列。

4. Executors.newWorkStealingPool()
拥有多个任务队列（以便减少连接数）的线程池。

6. Executors.newScheduledThreadPool(int corePoolSize)
初始化的线程池可以在指定的时间内周期性的执行所提交的任务，在实际的业务场景中可以使用该线程池定期的同步数据。队列使用了 DelayedWorkQueue 。
这是一个可延时执行阻塞任务的队列。

# 线程池的工作机制及其原理
## 线程池中的核心队列
1. BlockingQueue : 用于保存将要执行的任务并将其传递给工作中的线程队列。存储等待执行的任务
2. HashSet<Worker> : 包含所有的持有锁的工作线程。

## 线程池核心参数
1. corePoolSize : 线程池的基本大小。创建线程池后，默认情况下线程池中的线程个数0，当任务来了之后，就会去创建一个线程去执行
任务，当线程数量达到 corePoolSize 后就会把新任务放到 BlockingQueue 队列中去。

2. maximumPoolSize : 线程池最大线程个数。当线程数大于 corePoolSize，小于 maximumPoolSize 线程会被释放。

3. keepAliveTime 和 unit : 线程空闲后的存活时间。只有当线程数大于 corePoolSize 时，keepAliveTime 才会起作用。

4. workQueue : 用于存放任务的阻塞队列。

5. handler : 当队列和最大线程池都满了之后的饱和拒绝策略。
```text
ThreadPoolExecutor.AbortPolicy : 直接抛出异常
ThreadPoolExecutor.DiscardPolicy : 不处理，丢弃掉
ThreadPoolExecutor.DiscardOldestPolicy : 丢弃队列里最近的一个任务，并执行当前任务
ThreadPoolExecutor.CallerRunsPolicy : 只用调用者所在的线程来运行任务
```

#  使用 Hystrix 来隔离线程池
[参考](http://ifeve.com/%E5%A6%82%E4%BD%95%E4%BC%98%E9%9B%85%E7%9A%84%E4%BD%BF%E7%94%A8%E5%92%8C%E7%90%86%E8%A7%A3%E7%BA%BF%E7%A8%8B%E6%B1%A0/)

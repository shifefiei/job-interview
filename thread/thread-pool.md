### 线程池核心参数解释
线程池的核心思想是把宝贵的线程资源放到一个池子中，每次使用的时候就去池子中取，用完之后又放回去。
1. 常见的线程池
```text
    Executors.newSingleThreadExecutor() 创建单个线程，池子里只有一个线程，如果线程因为异常结束，会新建一个线程，使用 LinkedBlockingQueue 队列。

    Executors.newCachedThreadPool() 创建一个可伸缩的线程池，将复用可用线程，如果没有可用线程，则创建一个添加到池子中去，60s中未使用的线程将被移除， 使用 SynchronousQueue 队列。

    Executors.newFixedThreadPool(int nThread) 创建固定大小的线程池，使用的是 LinkedBlockingQueue 队列。

    Executors.newWorkStealingPool()

    Executors.newSingleThreadScheduledExecutor()

    Executors.newScheduledThreadPool(int corePoolSize)
```

2. 线程池的工作机制及其原理<br/>
(1) 线程池中的核心队列 <br/>
    BlockingQueue : 用于保存将要执行的任务并将其传递给工作中的线程队列。存储等待执行的任务 <br/>
    HashSet : 包含所有的持有锁的工作线程 <br/>
(2) 线程池核心参数
```text
    corePoolSize : 线程池的基本大小。创建线程池后，默认情况下线程池中的线程个数0，当任务来了之后，就会去创建一个线程去执行任务，
    当线程数量达到 corePoolSize 后就会把新任务放到 BlockingQueue 队列中去。
    
    maximumPoolSize : 线程池最大线程个数。当线程数大于 corePoolSize，小于 maximumPoolSize 线程会被释放。
    
    keepAliveTime 和 unit : 线程空闲后的存活时间。只有当线程数大于 corePoolSize 时，keepAliveTime 才会起作用。
    
    workQueue : 用于存放任务的阻塞队列。
    
    handler : 当队列和最大线程池都满了之后的饱和拒绝策略。
    
    ThreadPoolExecutor.AbortPolicy : 直接抛出异常
    ThreadPoolExecutor.DiscardPolicy : 不处理，丢弃掉
    ThreadPoolExecutor.DiscardOldestPolicy : 丢弃队列里最近的一个任务，并执行当前任务
    ThreadPoolExecutor.CallerRunsPolicy : 只用调用者所在的线程来运行任务
```

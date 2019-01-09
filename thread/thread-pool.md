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

2. 线程池的工作机制及其原理
(1) 线程池中的核心队列
    BlockingQueue : 用于保存将要执行的任务并将其传递给工作中的线程队列。存储等待执行的任务
    HashSet : 包含所有的持有锁的工作线程
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
(3) 线程池工作原则<br/>
    - 当线程池中线程数量小于 corePoolSize 则创建线程，并处理请求 <br/>
    - 当线程池中线程数量大于等于 corePoolSize 时，则把请求放入 workQueue 中,随着线程池中的核心线程们不断执行任务，只要线程池中有空闲的核心线程，线程池就从workQueue 中取任务并处理 <br/>
    - 当 workQueue 已存满，放不下新任务时则新建非核心线程入池，并处理请求直到线程数目达到 maximumPoolSize（最大线程数量设置值）<br/>
    - 如果线程池中线程数大于 maximumPoolSize 则使用 RejectedExecutionHandler 来进行任务拒绝处理 <br/>

3. [线程池参数预估](http://ifeve.com/how-to-calculate-threadpool-size/)
    (1) 如何来设置<br/>
        - 需要根据几个值来决定<br/>
            - tasks ：每秒的任务数，假设为500~1000 <br/>
            - taskcost：每个任务花费时间，假设为0.1s <br/>
            - responsetime：系统允许容忍的最大响应时间，假设为1s <br/>
        - 做几个计算 <br/>
            - corePoolSize = 每秒需要多少个线程处理 <br/>
                * threadcount = tasks/(1/taskcost) =tasks*taskcout =  (500~1000)*0.1 = 50~100 个线程。corePoolSize设置应该大于50 <br/>
                * 根据8020原则，如果80%的每秒任务数小于800，那么corePoolSize设置为80即可 <br/>
            - queueCapacity = (coreSizePool/taskcost)*responsetime,阻塞队列的大小 <br/>
                * 计算可得 queueCapacity = 80/0.1*1 = 80。意思是队列里的线程可以等待1s，超过了的需要新开线程来执行 <br/>
                * 切记不能设置为Integer.MAX_VALUE，这样队列会很大，线程数只会保持在corePoolSize大小，当任务陡增时，不能新开线程来执行，响应时间会随之陡增。<br/>
            - maxPoolSize = (max(tasks)- queueCapacity)/(1/taskcost) <br/>
                * 计算可得 maxPoolSize = (1000-80)/10 = 92 <br/> 
                * （最大任务数-队列容量）/每个线程每秒处理能力 = 最大线程数 <br/>
            - rejectedExecutionHandler：根据具体情况来决定，任务不重要可丢弃，任务重要则要利用一些缓冲机制来处理 <br/>
            - keepAliveTime和allowCoreThreadTimeout采用默认通常能满足 <br/>

    (2) 以上都是理想值，实际情况下要根据机器性能来决定。
    如果在未达到最大线程数的情况机器cpu load已经满了，则需要通过升级硬件和优化代码，降低taskcost来处理。

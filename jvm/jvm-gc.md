### jvm如何判断对象已经死亡
- 引用计数法: 给内存对象添加一个引用计数器，有引用该对象时值加 1，直到对象引用计数器为 0 时
- 可达性分析算法 ：

### 垃圾回收算法
- 标记清除算法
- 复制算法
- 标记整理算法
- 分代收集算法

### 各种垃圾收集器

- Serial ['sɪərɪəl]收集器
新生代收集器，使用复制算法，使用一个线程进行GC，串行，其它用户工作线程暂停。该收集器简单高效。

- ParNew [pɑː][njuː]收集器
新生代收集器，使用复制算法，Serial收集器的多线程版，用多个线程进行GC，并行，其它工作线程暂停。 
使用-XX:+UseParNewGC开关来控制使用ParNew+Serial Old收集器组合收集内存；使用-XX:ParallelGCThreads来设置执行内存回收的线程数。

- Parallel Scavenge 收集器
吞吐量优先的垃圾回收器，作用在新生代，使用复制算法，关注CPU吞吐量，即运行用户代码的时间/总时间。 
使用-XX:+UseParallelGC开关控制使用Parallel Scavenge+Serial Old收集器组合回收垃圾。

- Serial Old收集器
老年代收集器，单线程收集器，串行，使用标记整理算法，使用单线程进行GC，其它工作线程暂停。

- Parallel Old ['pærəlel]收集器
吞吐量优先的垃圾回收器，作用在老年代，多线程，并行，多线程机制与Parallel Scavenge差不错，使用标记整理算法，在Parallel Old执行时，仍然需要暂停其它线程。 

- CMS（Concurrent Mark Sweep）收集器
老年代收集器，致力于获取最短回收停顿时间（即缩短垃圾回收的时间），使用标记清除算法，多线程，优点是并发收集（用户线程可以和GC线程同时工作），停顿小。
使用-XX:+UseConcMarkSweepGC进行ParNew+CMS+Serial Old进行内存回收，优先使用ParNew+CMS（原因见Full GC和并发垃圾回收一节），当用户线程内存不足时，采用备用方案Serial Old收集。
    <br/>
    缺点：对cpu资源铭感，产生内存碎屏，引发full gc

- G1收集器 :
基于 标记-整理 算法进行垃圾回收。<br/>
    1. 并行、并发收集
    2. 分带收集，同时兼顾老年代和新生代，G1进行垃圾回收时，会将内存区域划分为多个 Region 区域，新生代、老年代不再是屋里屏障。
    3. 空间整合，垃圾回收后，不会产生内存碎步，可提供连续完整的内存空间
    4. 可预测停顿，停顿时间小
    
    哪些场景下使用G1收集器？
    <br/>
    (1) 堆内存的占用率达到50%以上时刻考虑使用
    (2) GC 停顿时间过长

[jvm](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/05-Java%E8%99%9A%E6%8B%9F%E6%9C%BA.md#4-%E5%9E%83%E5%9C%BE%E6%94%B6%E9%9B%86%E5%99%A8)
[参考文章](https://segmentfault.com/a/1190000016152186)

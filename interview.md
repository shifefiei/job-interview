### HashMap如何解决冲突

加载因子越大,填满的元素越多,好处是,空间利用率高了,但冲突的机会加大了.链表长度会越来越长,查找效率降低。
所以引入红黑树？原因：
(1)大的链表表遍历很耗时，使用树的话时间复杂度从O(n)降到了O(logN)
(2)JDK1.8之前的版本大家都知道是put过程中的resize方法在调用transfer方法的时候导致的死锁

1. 什么是hash冲突：两个相同的对象hashCode值相同，往我们的map里面存储的时候在同一个桶位置上，此时该如何处理？
- 开放定址法 ：p=h(x),再对p值hash
- 再哈希法 ：利用不同的hash函数对冲突的元素再hash
- 链地址法 : jdk 1.7中的链表添加

2. put 方法是如何插入元素
(1)hash后的元素对应的位置上是否有元素，没有直接添加元素<br/>
(2)有的话先会判断是不是同一个元素，是同一个就覆盖<br/>
(3)不是的话会判断是否可以转化成红黑树树，否则直接添加

3. get 方法 <br/>
(1) 拿到key的hash值 <br/>
(2) 检查是是第一个元素，是直接返回 <br/>
(3) 遍历链表，判断是否是树，如果是去查找树

4. 那么HashMap根据hashcode是如何得到数组下标呢？可以拆分为以下几步：
    (1) h = key.hashCode() <br/>
    (2) h ^ (h >>> 16) : 高16位 异或 低16位,保证高位低位都能参与到计算中 <br/>
    (3) (length - 1) & hash ：对length 取模

```java
class HashMap {
    
        //初始桶容量是 16
        static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; 
    
        //最大桶容量 1<<30 = 2^30
        static final int MAXIMUM_CAPACITY = 1 << 30;
   
        /**
         * 负载因子
         * 作用：给定的默认容量为 16，负载因子为 0.75。Map 在使用过程中不断的往里面存放数据，
         * 当数量达到了 16 * 0.75 = 12 就需要将当前 16 的容量进行扩容，而扩容这个过程涉及到 
         * rehash、复制数据等操作。
         */
        static final float DEFAULT_LOAD_FACTOR = 0.75f;
        //用于判断是否需要将链表转换为红黑树的阈值
        static final int TREEIFY_THRESHOLD = 8;
        //用于判断是否需要将红黑树转换为链表的阈值
        static final int UNTREEIFY_THRESHOLD = 6;
        //哈希表的最小树形化容量
        static final int MIN_TREEIFY_CAPACITY = 64;
        //桶的数量
        transient Node<K,V>[] table;
        transient Set<Map.Entry<K,V>> entrySet;
    
        //元素的个数
        transient int size;
        //hashMap 结构被改变的次数记录，比如扩容操作
        transient int modCount;
    
        int threshold; //判断是否扩容，当元素数量达到了 16 * 0.75 = 12
    
        final float loadFactor; //加载因子，0.75f
        
    public V put(K key, V value) {
            return putVal(hash(key), key, value, false, true);
    }

    static final int hash(Object key) {
            int h;
            return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
    
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                       boolean evict) {
            Node<K,V>[] tab; Node<K,V> p; int n, i;
            //判断数组是否空或者长度0，是则进行初始化
            if ((tab = table) == null || (n = tab.length) == 0)
                n = (tab = resize()).length;
            //判断是否有冲突，数组位置上无元素，则创建节点添加进去
            if ((p = tab[i = (n - 1) & hash]) == null)
                tab[i] = newNode(hash, key, value, null);
            else {
                /**
                 * 冲突了怎么办？
                 * 三个if逻辑处理：
                 * 1.判断hash值和key是否相同
                 * 2.判断当前树是不是红黑树？(1)大的链表表遍历很耗时，使用树的话时间复杂度从O(n)降到了O(log2n)
                 * 3.遍历链表，链表空直接保存当前元素，否则判断链表长度是否达到阈值8，如果是则转化为树 
                 */
                Node<K,V> e; K k;
                if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k))))
                    e = p;
                else if (p instanceof TreeNode)
                    e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
                else {
                    for (int binCount = 0; ; ++binCount) {
                        if ((e = p.next) == null) {
                            p.next = newNode(hash, key, value, null);
                            //判断是否可以转化成红黑树
                            if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                                treeifyBin(tab, hash);
                            break;
                        }
                        if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                            break;
                        p = e;
                    }
                }
                if (e != null) { // existing mapping for key
                    V oldValue = e.value;
                    if (!onlyIfAbsent || oldValue == null)
                        e.value = value;
                    afterNodeAccess(e);
                    return oldValue;
                }
            }
            ++modCount;
            if (++size > threshold)
                resize();
            afterNodeInsertion(evict);
            return null;
        }
}
```

5. resize 方法如何扩容<br/>先插入元素在进行扩容
- jdk 1.7操作
```java
class HashMap {
    void resize(int newCapacity) {//传入新的容量  
        //引用扩容前的Entry数组
        Entry[] oldTable = table;      
        int oldCapacity = oldTable.length; 
        //扩容前的数组大小如果已经达到最大(2^30)了
        if (oldCapacity == MAXIMUM_CAPACITY) { 
            //修改阈值为int的最大值(2^31-1)，这样以后就不会扩容了
            threshold = Integer.MAX_VALUE;   
            return;  
        }  
      
        //初始化一个新的Entry数组
        Entry[] newTable = new Entry[newCapacity];    
        //将数据转移到新的Entry数组里 !!!
        transfer(newTable); 
        //HashMap的table属性引用新的Entry数组 
        table = newTable;
        //修改阈值
        threshold = (int) (newCapacity * loadFactor);  
    }  
    
    //拷贝数组元素
    void transfer(Entry[] newTable) {  
        Entry[] src = table;//src引用了旧的Entry数组                    
        int newCapacity = newTable.length;  
        for (int j = 0; j < src.length; j++) { //遍历旧的Entry数组  
            Entry<K, V> e = src[j];//取得旧Entry数组的每个元素  
            if (e != null) {  
                src[j] = null;//释放旧Entry数组的对象引用（for循环后，旧的Entry数组不再引用任何对象）  
                do {  
                    Entry<K, V> next = e.next;  
                    int i = indexFor(e.hash, newCapacity); //重新计算每个元素在数组中的位置 !!!  
                    e.next = newTable[i]; //单链表的头插入方式，同一位置上新元素总会被放在链表的头部位置
                    newTable[i] = e;      //将元素放在数组上  
                    e = next;             //访问下一个Entry链上的元素  
                } while (e != null);  
            }  
        }  
    } 
    
    //计算新数组索引
    int indexFor(int h, int length) {  
        return h & (length - 1);  
    }  
}
```
- jdk 1.8中的扩容操作
```java

class HashMap {
    Node<K,V>[] resize() {
            Node<K,V>[] oldTab = table;
            int oldCap = (oldTab == null) ? 0 : oldTab.length;
            int oldThr = threshold;
            int newCap, newThr = 0;
            //如果有容量,说明该map已经有元素
            if (oldCap > 0) { 
                if (oldCap >= MAXIMUM_CAPACITY) {
                    threshold = Integer.MAX_VALUE;
                    return oldTab;
                }
                //在此处newCap = oldCap << 1，容量翻倍了
                else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                         oldCap >= DEFAULT_INITIAL_CAPACITY)
                    newThr = oldThr << 1; // double threshold
            }
            else if (oldThr > 0) // initial capacity was placed in threshold
                newCap = oldThr;
            else {               // zero initial threshold signifies using defaults
                newCap = DEFAULT_INITIAL_CAPACITY;
                newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
            }
            if (newThr == 0) {
                float ft = (float)newCap * loadFactor;
                newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                          (int)ft : Integer.MAX_VALUE);
            }
            threshold = newThr;
             @SuppressWarnings({"rawtypes","unchecked"})
               Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
           table = newTab;
           /**
            * 上面一顿操作，最后总结就是：
            * 1> 如果是最开始还没有元素的情况：
            *     1、如果初始化的时候带了参数
            *         （HashMap(int initialCapacity, float loadFactor)），
            *         那么newCap就是你的initialCapacity参数
            *         threshold就是 (int)(initialCapacity*loadFactor)
            *     2、否则就按默认的算 initialCapacity = 16，threshold = 12
            * 2> 如果已经有元素了，那么直接扩容2倍，如果oldCap >= DEFAULT_INITIAL_CAPACITY了，那么threshold也扩大两倍
            */
           if (oldTab != null) {
               //将原来map中非null的元素rehash之后再放到newTab里面去
               for (int j = 0; j < oldCap; ++j) {
                   Node<K,V> e;
                   if ((e = oldTab[j]) != null) {
                       oldTab[j] = null;
                       //如果这个oldTab[j]就一个元素，那么就直接放到newTab里面
                       if (e.next == null) 
                           newTab[e.hash & (newCap - 1)] = e;
                       else if (e instanceof TreeNode)
                           //如果原来这个节点已经转化为红黑树了，
                           //那么我们去将树上的节点rehash之后根据hash值放到新地方
                           ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                       else { // preserve order
                       /**
                        * 这里的操作就是 (e.hash & oldCap) == 0 这一句，
                        *  这一句如果是true，表明(e.hash & (newCap - 1))还会和
                        *  e.hash & (oldCap - 1)一样。因为oldCap和newCap是2的次幂，
                        *  并且newCap是oldCap的两倍，就相当于oldCap的唯一
                        *  一个二进制的1向高位移动了一位
                        *  (e.hash & oldCap) == 0就代表了(e.hash & (newCap - 1))还会和
                        *  e.hash & (oldCap - 1)一样。
    
                        *  比如原来容量是16，那么就相当于e.hash & (00010000) 
                        *  （ 00001111 就是 oldCap - 1 = 16 - 1 = 15），
                        *  现在容量扩大了一倍，就是32，那么rehash定位就等于
                        *  e.hash & （00011111） （00011111 就是newCap - 1 = 32 - 1 = 31）
                        *  现在(e.hash & oldCap) == 0 就表明了
                        *  e.hash & （00010000） == 0，这样的话，不就是
                        *  已知： e.hash &  （00011111） = hash定位值Value
                        *  并且  e.hash & （00010000） = 0
                        *  那么   e.hash & （00011111） 不也就是
                        *  原来的hash定位值Value吗
    
                        *  那么就只需要根据这一个二进制位就可以判断下次hash定位在
                        *  哪里了。将hash冲突的元素连在两条链表上放在相应的位置
                        *  不就行了嘛
                        */
                          Node<K,V> loHead = null, loTail = null;
                          Node<K,V> hiHead = null, hiTail = null;
                          Node<K,V> next;
                          do {
                              next = e.next;
                              if ((e.hash & oldCap) == 0) {
                                  if (loTail == null)
                                      loHead = e;
                                  else
                                      loTail.next = e;
                                  loTail = e;
                              }
                              else {
                                  if (hiTail == null)
                                      hiHead = e;
                                  else
                                      hiTail.next = e;
                                  hiTail = e;
                              }
                          } while ((e = next) != null);
                          if (loTail != null) {
                              loTail.next = null;
                              newTab[j] = loHead;
                          }
                          if (hiTail != null) {
                              hiTail.next = null;
                              newTab[j + oldCap] = hiHead;
                          }
                      }
                  }
              }
          }
          return newTab;
  }
}

```
<br/>[参考文章](https://blog.csdn.net/runrun117/article/details/80249556)<br/>
    


### ConcurrentHashMap 原理

#### jdk 1.7 中的 ConcurrentHashMap
它的数据接口是数组 + 链表，使用了分段锁解决了并发问题 Segment<K,V>[] segments。


- 改进一：取消segments字段，直接采用 transient volatile Node<K,V>[] table 保存数据，
  采用table数组元素作为锁，从而实现了对每一行数据进行加锁，进一步减少并发冲突的概率。
  
- 改进二：将原先table数组＋单向链表的数据结构，变更为table数组＋单向链表＋红黑树的结构。这种结构将时间复杂度可以降低到O(logN)。

### jdk 1.8 ConcurrentHashMap


### ConcurrentHashMap 扩容两个链表相交的判断


### synchronized加在static方法和非static方法上区别
- 表示此时的lock锁对象不一样，static 方法的锁是当前类的Class对象，而非static方法的锁是当前调用该方法的对象，它们之间不会产生互斥

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

### spring aop的原理和实现
#### [代理模式](https://github.com/shifefiei/dp-review/blob/master/dp-proxy/src/main/resources/proxy.md)
### spring aop
1. 什么是 spring aop?
spring aop 都知道是面向切面编程，主要的实现手段有 spring-aop 和 AspectJ

- spring aop 
spring aop 采用的是动态代理模式实现的，即利用jdk动态代理和cglib动态代理
(1) jdk代理的实现利用了java.lang.reflect包的Proxy类的newProxyInstance方法 以及 InvocationHandler 接口来调用具体方法
(2) cglib 依赖spring-core核心包，是一个代码生成类库，可以在运行时动态的生成某个类的子类

spring aop默认使用的是 jdk 动态代理，如果代理的类没有实现接口则会使用cglib代理 
- spring aop的通知类型
(1) Before : 前置通知，目标方法调用之前执行
(2) After : 后置通知，目标方法完成之后执行，不关心方法结果
(3) After-returning : 返回通知，目标方法成功执行之后通知
(4) After-throwing : 异常通知，目标方法抛出异常后通知
(5) Around : 环绕通知，方法调用之前和之后执行


### spring ioc的原理和实现
- ioc 和 di 的区别<br/>
ioc 控制反转，指将对象的创建权，反转到spring容器 ; di 依赖注入，指spring创建对象的过程中，将对象依赖属性通过配置进行注入

- ioc 原理<br/>
我理解的ioc容器主要功能就是对 spring bean 的管理的过程，spring ioc 的核心组件：
1. BeanFactory : 定义了一个简单的 ioc 容器，spring 在 BeanFactory的基础上可以扩展，主要是管理bean的加载、实例化等
2. ApplicationContext ：对于 ApplicationContext 而言，则扩展了BeanFactory ,提供了更多的额外功能，比如：ClassPathXmlApplicationContext
3. BeanDefinition ：它定义了每个 bean 的信息，比如 bean 的属性，类名，类型等的信息

- bean 的生命周期
1. spring 对 bean 初始化
2. spring bean 属性填充
3. 调用BeanNameAware接口的setBeanName()方法，如果实现了该接口
4. 调用BeanFactoryAware接口的setBeanFactory()方法，如果实现了该接口
5. 调用ApplicationContextAware接口的setApplicationContext()方法
6. 调用BeanPostProcessor接口的postProcessBeforeInitialization()方法
7. 调用InitializingBean接口的afterPropertiesSet()方法
8. 调用BeanPostProcessor接口的postProcessAfterInitialization()方法
9. bean准备就绪
10. 调用DisposableBean接口的destroy方法

### spring bean实例化的方式有哪些
- 使用类的构造器
- 简单工厂模式
- 工厂方法模式实例化，先创建工厂实例beanFactory，再通过工厂实例创建目标bean实例

### spring bean注入属性的方式 
- 接口注入
- 构造器注入
- setter方法注入

### spring 中用到了那些设计模式
- [工厂方法模式](https://github.com/shifefiei/dp-review/blob/master/dp-factory/src/main/resources/factory.md)，比如各种 BeanFactory
- [单例模式](https://github.com/shifefiei/dp-review/blob/master/dp-singleton/src/main/resources/singleton.md)，spring 默认单例模式
- 策略模式，例如：当bean需要访问资源配置文件时就会用到策略模式。Resource 接口封装了各种可能的资源类型，
包括了：UrlResource，ClassPathResource，FileSystemResource等，Spring需要针对不同的资源采取不同的访问策略。
在这里，Spring让ApplicationContext成为了资源访问策略的“决策者”
- [代理模式](https://github.com/shifefiei/dp-review/blob/master/dp-proxy/src/main/resources/proxy.md)



### mysql基本索引存储结构?什么是联合索引？




### 基本命令了解，统计某个文件中某个人登录次数，不ok;

### git没用过，用的svn；maven解决冲突



#### String StringBuffer StringBuilder 区别String ok StringBuffer 安全 StringBuilder不安全 stringbuilder使用场景不知道

### object equals == 区别  和 String equals == 区别

### 二叉树结构：不太了解了


### 线程的状态

状态：就绪 运行 结束 等待 堵塞；

sleep wait 区别：sleep 不释放锁 wait 释放锁 notify后流程：先获取锁再执行

sync : 底层原理不清楚, 加在不同地方: 锁类、锁对象 理解不对

lock : 与sync区别：不知道 使用场景：没使用

volatile：可见性、原子性

automic:原理没研究过

ThreadLocal：原理OK；子线程用父线程TheadLocal对象：可以，不知道如何实现


IOC：默认：多例 

AOP: 动态代理：jdk cglib 区别

bean 循环依赖：没遇到场景

事务传播：原理基本知道AOP实现 


OOM: 不清楚
回收机制：没有细研究过

策略：场景题：计算器设计 

是否走索引：不知道


3.spring aop实现方式了解，ok；但是，对cglib没用过，不清楚优缺点；不OK

5.基本命令了解 grep，top，netstat等使用过，一般;

6.git基本没使用过；


GC垃圾回收算法，OK

5.设计模式：基本OK

6.mysql:数据量不大，没有优化经验，不OK。

索引：不了解内部数据结构。

7.mq无法说清楚内部结构。

dubbo:基本了解。一般

8.知道redis基础数据结构，一般。

thrift:
https://www.cnblogs.com/chenny7/p/4224720.html


6.
引擎:innodb\myisam 
区别：
 锁： innoDB行级锁，myisam表级
 事务：innoDB支持,myisam不支持，
 索引：innoDB BTree，myisam 压缩 不OK
隔离级别：4种OK 幻读含义：回答不正确 不可重复读：回答不正确 不OK
索引：explain
联合索引: 走索引方式 回答基本OK


栈内存溢出程序：无法写出。不OK。



6.索引数据结构，Btree，hash，btree索引的适用范围。不OK。

聚簇索引和非聚簇索引之间的区别，不ok。

7.查看日志命令，cat，grep，top，chmod等，ok。

jdk提供的命令有了解，jstack查看进程运行状态但是，jmap和jstat之间区别记得不够清楚。

8.jar包冲突，rebase和reset之间区别，不ok


1.
String StringBuffer StringBuilder 区别OK，StringBuffer是线程安全的,StringBuilder是线程不安全的 常用 StringBuffer StringBuilder 使用场景不太明确
HaspMap 结构：数据+链表；key算法：不知道 
concurrenthashmap: 线程安全的。比hashTable效率高 锁分段算法：没看过
String equals ==: 比较方式回答不对
tree: 数据结构 OK 遍历算法：不清晰


事务传播：原理基本知道AOP实现，事务应用 OK

6.oracle、mysql索引数据结构：没关注过
引擎:不知道
隔离机制：不知道


3.线程安全的单例模式：无法写出。对于其他设计模式，只知道基本名称。不OK

4.mysql索引数据结构及优化：能够基本使用，底层实现不清楚。不OK

5.排序算法：知道插入和快排思路。OK

6.JDK8的特性，不了解。不OK



1.数据库分库 分表 事物控制
2.redis缓存及集群方式
3.IO模式原理及应用场景
4.多线程 ThreadLocal  volidate  atomic原子型原理及在项目中的应用
5.大并发  设计  mq  ngix  redis  数据集多主多重  zookeeper 然后延展问原理及内部机   制
6.分布式事务锁
7.jvm优化
8.前段Ajax 及HTTP之间多线程访问的问题
9. tomcat集群及并发支持量
10.系统安全机制用的是什么
11. mq 数据丢失问题
12.


### static 和 final 关键字
1. static关键字
一、static的使用
1、static用于修饰成员变量和成员方法，称之为静态变量和静态方法，可以直接通过类名访问。
2、static修饰的代码块称之为静态代码块，在类加载期间就会执行代码块内容。

二、static变量
static修饰的变量称之为静态变量，没有用static修饰的变量称之为实例变量

（1）静态变量是随着类加载时被完成初始化的，它在内存中仅有一个，且 JVM 也只会为它分配一次内存，同时类所有的实例都共享静态变量，可以直接通过类名来访问它。
（2）实例变量则不同，它是伴随着实例的，每创建一个实例就会产生一个实例变量，它与该实例同生共死。

三、static方法
static修饰的方法称之为静态方法，通过类名之间调用，不依赖与任何实例；

四、static代码块
被 static 修饰的代码块，我们称之为静态代码块，静态代码块会随着类的加载一块执行，而且他可以随意放，可以存在于该了的任何地方。

五、static的局限性
（1）它只能调用static变量
（2）不能以任何形式引用this、super
（3）static 变量在定义时必须要进行初始化，且初始化时间要早于非静态变量。

2. final关键字
使用到final的有三种情况：变量、方法和类；

一、final变量：它修饰的变量在初始化后不能再修改了
（1）编译期常量：
它在类加载的过程就已经完成了初始化，所以当类加载完成后是不可更改的，对于编译期常量，只能使用基本类型，而且必须要在定义时进行初始化。
（2）运行期常量
对于运行期常量，它既可是基本数据类型，也可是引用数据类型。基本数据类型不可变的是其内容，而引用数据类型不可变的是其引用，引用所指定的对象内容是可变的。

二、final方法
final修饰的方法不能被重新

三、final类
final休息的类不能够被继承

四、final修饰的参数，表示该参数是不可变的
在内部类中，final是很有用的；如果我们定义了一个匿名内部类，并且希望使用一个外部类的参数，该参数要加final关键字修饰；为了避免引用值发生改变，
例如被外部类的方法修改等，而导致内部类得到的值不一致；


















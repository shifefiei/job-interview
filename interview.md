### HashMap如何解决冲突

加载因子越大,填满的元素越多,好处是,空间利用率高了,但冲突的机会加大了.链表长度会越来越长,查找效率降低。
所以引入红黑树？原因：
(1)大的链表表遍历很耗时，使用树的话时间复杂度从O(n)降到了O(log2n)
(2)JDK1.8之前的版本大家都知道是put过程中的resize方法在调用transfer方法的时候导致的死锁

1. 什么是hash冲突：两个相同的对象hashCode值相同，往我们的map里面存储的时候在同一个桶位置上，此时该如何处理？
- 开放定址法 ：p=h(x),再对p值hash
- 再哈希法 ：利用不同的hash函数对冲突的元素再hash
- 链地址法 : jdk 1.7中的链表添加

put是如何放元素：hash后的元素对应的位置上是否有元素，没有直接添加元素；有的话先会判断是不是同一个元素，是的话就覆盖，不是的话会判断是否可以转化
成树，否则直接添加

```java
class HashMap {
    
        //初始桶容量是 16
        static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; 
    
        //最大桶容量 1<<30.
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



线程安全的hashmap,concurrentHashmap了解原理

synchronized加在static方法和非static方法上区别：不ok；

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


2.线程池核心参数了解意义,不ok；

3.spring aop实现方式了解，不ok；通过反射，动态代理（java动态代理，cglib动态代理）？？？

4.mysql基本索引存储结构,二叉树，不ok；

联合索引，不ok

5.基本命令了解，统计某个文件中某个人登录次数，不ok;

6.git没用过，用的svn；maven解决冲突，不ok；

7.jvm没怎么用过，不了解。




1.
String StringBuffer StringBuilder 区别String ok StringBuffer 安全 StringBuilder不安全 stringbuilder使用场景不知道
HaspMap 看过部分原码 深层就不知道了
concurrenthashmap: 没有看过及用过
object equals == 区别：OK String equals ==: ok
二叉树结构：不太了解了
2.
状态：就绪 运行 结束 等待 堵塞； sleep wait 区别：sleep 不释放锁 wait 释放锁 notify后流程：先获取锁再执行 
sync : 底层原理不清楚, 加在不同地方: 锁类、锁对象 理解不对
lock : 与sync区别：不知道 使用场景：没使用
volatile：可见性、原子性
automic:原理没研究过
ThreadLocal：原理OK；子线程用父线程TheadLocal对象：可以，不知道如何实现

3.
IOC：默认：多例 
AOP: 动态代理：jdk cglib 区别：ok
bean 循环依赖：没遇到场景

4.
jvm : 了解不多
OOM: 不清楚
回收机制：没有细研究过

5.
策略：场景题：计算器设计 

6.
是否走索引：不知道



1.HashMap的解决冲突了解，一般

线程安全的hashmap,concurrentHash了解原理。不OK

synchronized加在static方法和非static方法上区别：基本不知道，不ok；

2.线程池核心参数了解意义,不ok；

3.spring aop实现方式了解，ok；但是，对cglib没用过，不清楚优缺点；不OK

4.mysql基本索引存储结构,不知道 ，不ok；

5.基本命令了解，grep，top，netstat等使用过，一般;

6.git基本没使用过，用的svn；





OC：基本说清内部结构，
AOP: 基本说清动态代理，一般。

 4.jvm : 内部结构基本了解，调优参数知道，一般。

GC垃圾回收算法，OK

5.设计模式：基本OK

6.mysql:数据量不大，没有优化经验，不OK。

索引：不了解内部数据结构。

7.mq无法说清楚内部结构，不OK。

dubbo:基本了解。一般

8.知道redis基础数据结构，一般。




1.
String StringBuffer StringBuilder 区别String ok StringBuffer线程不安全 StringBuilder线程安全 记混了 常用StringBuffer 不OK
HaspMap 结构：数据+链表；下标算法：不知道；新元素放到链表位置：头部 不OK
HashMap与HashTable: HashTable是线程安全的，加锁方式：不确定是sync还是lock 不OK
concurrenthashmap: 比HashMap并发好。效率高 锁分段算法：不知道 不OK
object equals == 区别：OK String equals ==: ok
二叉树结构：对数据结构比较弱 不OK
2.
状态：就绪 运行 结束 等待 堵塞； sleep wait 区别：sleep 释放锁 wait 不释放锁 不OK
sync : 原理不太了解, 加在不同地方: 不OK
volatile：可见性、原子性，应用场景：比较模糊
ThreadLocal：原理OK；子线程用父线程TheadLocal对象：不可以（回答不正确）

3.
IOC：默认：单例 可以多例 
AOP: 动态代理：jdk cglib 区别：不知道 不OK
bean 循环依赖：没研究过
事务传播：原理基本知道AOP实现 
4.
OOM: PC不会引起
回收器：不太了解 不OK
回收对象机制：了解比较浅 不OK
5.
策略：场景题：计算器设计 不OK

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
2.
状态：新建 就绪 运行 堵塞 死亡； sleep wait 区别：sleep 不释放锁 wait 释放锁 notify：直接就绪，等待CPU执行 不OK
线程等待：join 
sync : 底层原理不知道
lock : 与sync区别没了解过
volatile：没用过
threadLocal：没用过
atomic：没听过
3.
IOC：概念OK，默认：单例 可以多例，但不实现过
AOP: 动态代理：jdk cglib 区别：不知道
bean 循环依赖：SPRING解决方案 没看过 
事务传播：原理基本知道AOP实现，事务应用 OK
4.
jvm 不太了解
5.
策略：不知道，应用不OK

6.
oracle、mysql索引数据结构：没关注过
引擎:不知道
隔离机制：不知道
mysql联合索引: 走索引方式 不OK


1.java基础：知道HashMap,ArayList,linkedList底层实现；OK

2.JVM内存模型：了解基本的分区分代，知道基本的堆内存和栈内存；一般；

3.线程安全的单例模式：无法写出。对于其他设计模式，只知道基本名称。不OK

4.mysql索引数据结构及优化：能够基本使用，底层实现不清楚。不OK

5.排序算法：知道插入和快排思路。OK

6.JDK8的特性，不了解。不OK

7.缓存：知道redis的基本使用.一般


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
12.concurrenthashmap 扩容，两个链表相交判断
### HashMap如何解决冲突

加载因子越大,填满的元素越多,好处是,空间利用率高了,但冲突的机会加大了.链表长度会越来越长,查找效率降低。
所以引入红黑树？原因：<br/>
(1)大的链表遍历很耗时，使用树的话时间复杂度从O(n)降到了O(logN)<br/>
(2)JDK1.8之前的版本大家都知道是put过程中的resize方法在调用transfer方法的时候导致的死锁

1. 什么是hash冲突：两个相同的对象hashCode值相同，往我们的map里面存储的时候在同一个桶位置上，此时该如何处理？
- 开放定址法 ：p=h(x),再对p值hash
- 再哈希法 ：利用不同的hash函数对冲突的元素再hash
- 链地址法 : jdk 1.7中的链表添加

2. put 方法是如何插入元素
(1)hash后的元素对应的位置上是否有元素，没有直接添加元素<br/>
(2)有的话先会判断是不是同一个元素，是同一个就插入到链表表尾<br/>
(3)不是的话会判断是否可以转化成红黑树树，否则直接添加

3. get 方法 <br/>
(1) 拿到key的hash值 <br/>
(2) 检查是是第一个元素，是直接返回 <br/>
(3) 遍历链表，判断是否是树，如果是去查找树

4. 那么HashMap根据hashcode是如何得到数组下标呢？可以拆分为以下几步：<br/>
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
### ConcurrentHashMap 原理

#### jdk 1.7 中的 ConcurrentHashMap
它的数据接口是数组 + 链表，使用了分段锁解决了并发问题 Segment<K,V>[] segments。


- 改进一：取消segments字段，直接采用 transient volatile Node<K,V>[] table 保存数据，
  采用table数组元素作为锁，从而实现了对每一行数据进行加锁，进一步减少并发冲突的概率。
  
- 改进二：将原先table数组＋单向链表的数据结构，变更为table数组＋单向链表＋红黑树的结构。这种结构将时间复杂度可以降低到O(logN)。

#### jdk 1.8 ConcurrentHashMap
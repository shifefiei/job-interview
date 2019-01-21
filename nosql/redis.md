## [redis复习](http://wiki.jikexueyuan.com/project/redis/)

### redis的数据类型
1. String（字符串），可以存储任何数据,在 Redis 中字符串类型的值最大只能保存 512 MB。
2. Hash（哈希表），键值对集合
3. List（列表），底层是双向链表
4. Set（集合）
5. ZSet（有序集合）


### Redis的数据结构和对象：
1. 简单动态字符串
2. 双向链表
3. 字典
4. 跳跃表，Redis使用跳跃表作为有序集合键底层的实现；

### Redis的持久化
1. Redis持久化方式<br/>
（1）RDB持久化：指定的时间间隔内生成数据集的时间点快照；<br/>
（2）AOF持久化：记录服务器执行的所有写操作命令，并在服务器启动时，通过重新执行这些命令来还原数据集；

2. [RDB优缺点](http://redis.cn/topics/persistence.html) <br/>
RDB 是一个非常紧凑（compact）的文件，它保存了 Redis 在某个时间点上的数据集。 这种文件非常适合用于进行备份；
适合灾难恢复；RDB 在恢复数据集时的速度比 AOF 的恢复速度要快。会丢失较长时间的数据；
3. AOF优缺点<br/>
使用 AOF 持久化会让 Redis 变得非常耐久，可以没秒进行一次操作；但是文件体积比较大，但是丢失数据比较少；

### Redis复习参考文章
1. [Redis复习精讲](http://blog.jobbole.com/114050/?utm_source=blog.jobbole.com&utm_medium=relatedPosts)


### [redis面试题](https://mp.weixin.qq.com/s?__biz=MzAwMDU1MTE1OQ==&mid=2653549993&idx=1&sn=27240c1aa611c352a487ecee8ded050b&chksm=813a6431b64ded27fd43bd07d5245b672a6e03e7b7d4482ccef58510d97dfadc39d82d257416&mpshare=1&scene=23&srcid=07247wQ3gUtqXo3EEDXNgcVk%23rd)
- [redis面试题](https://mp.weixin.qq.com/s?__biz=MzAwMDU1MTE1OQ==&mid=2653549993&idx=1&sn=27240c1aa611c352a487ecee8ded050b&chksm=813a6431b64ded27fd43bd07d5245b672a6e03e7b7d4482ccef58510d97dfadc39d82d257416&mpshare=1&scene=23&srcid=07247wQ3gUtqXo3EEDXNgcVk%23rd)
- [redis面试题2](https://mp.weixin.qq.com/s?__biz=MzI4NTA1MDEwNg==&mid=2650768368&idx=1&sn=61972539ec7b0a0b68cce5f127174ae6&chksm=f3f93665c48ebf7322943d1974f63e883488c60bbb2e4414bccf6dfe9d8489ad58e1557ae270&mpshare=1&scene=23&srcid=0625sJRHndesZGlMHGPWxNZK%23rd)
- [redis主从同步](https://www.kancloud.cn/mayan0718/php/515287)
- [redis哨兵](https://juejin.im/post/5b7d226a6fb9a01a1e01ff64)

### Redis 相关的java客户端
1. Jedis <br/>
    github：https://github.com/xetorthio/jedis <br/>
    文档：https://github.com/xetorthio/jedis/wiki <br/>

2. Redisson <br/>
    github：https://github.com/redisson/redisson <br/>
    文档：https://github.com/redisson/redisson/wiki <br/>
    
3. Redis 学习文档<br/>
    [社区文档](http://redis.cn/documentation.html)
    [Redis翻译文档](http://doc.redisfans.com/index.html)



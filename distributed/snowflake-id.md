### 分布式ID的设计方案

1. 分布式ID要求：
- 全局唯一
- 有序增长

2. 目前业界常用的方案
- 基于数据库自增序列实现
- 基于开源的[snowflake](https://github.com/twitter-archive/snowflake),snowflake的结构如下图所示：
![snowflake](https://github.com/shifefiei/job-interview/blob/master/static/picture/snowflake.png)
整体长度64位，其中
(1)头部一位是正负标识位;<br/>
(2)高位时41位时间戳;<br/>
(3)后面是 10 位的 WorkerID，标准定义是 5 位数据中心，5位机器ID;<br/>
(3)最后12位是就是单位毫秒的可生成的序列号极限值;<br/>
snowflake 官方是通过 Scala 语言实现的，github上有很多其他语言的实现，
比如[Java版本的实现](https://github.com/relops/snowflake/tree/a99cf2e65e8138791c38a8ddda1a379e2ec3eb0f)

[参考文章](https://tech.meituan.com/MT_Leaf.html)
[参考文章](https://www.cnblogs.com/xuyuanjia/p/5888509.html)
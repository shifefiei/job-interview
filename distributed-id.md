### 分布式ID的设计方案

1. 分布式ID要求：
- 全局唯一
- 有序增长

2. 目前业界常用的方案
- 基于数据库自增序列实现
- 基于开源的[snowflake](https://github.com/twitter-archive/snowflake),snowflake的结构如下图所示：
![]
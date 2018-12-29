### mysql基本索引存储结构?什么是联合索引？
1. mysql默认使用innoDB引擎，索引的数据结构是B+树，B+树的特点：<br/>
    (1)所有关键字都出现在叶子结点的链表中，且链表中的关键字恰好是从小到大有序排列；
    (2)有n棵子树的结点中含有n个关键字，每个关键字不保存数据，只用来索引，所有数据都保存在叶子节点;
    (3)B+树结构：
    ![B+树](https://github.com/shifefiei/job-interview/blob/master/static/picture/B+树.png)
    
2. 为什么使用B+树？<br/>
(1)B+树中数据都保存在叶子节点，中间节点没有数据域，所以同样大小空间中B+树容纳的元素更多；
(2)B+树在查找元素时必须最终查询到叶子节点，所以B+查找很稳定，不像B-树那样匹配到就结束了；

3. 索引的分类<br/>
索引是一种特殊的文件，包含了对数据库中所有记录的引用指针<br/>
(1)组合索引，左匹配原则<br/>
    现有如下查询条件：select * from emp where a = x and b = y and c = z;(a,b,c联合索引) <br/>
    - = 和 in 可以乱序，优化后能够被识别
    - 在查询条件 where a = x and b > y and c = z 中，a,b 可以用到索引，但是 c 用不到，mysql会向右匹配，遇到 (>,<,between ... and 会停止)
(2)单列索引   <br/>
    - 普通索引,create index index_name on table(col)
    - 唯一索引，允许有控制,create unique index_name on table(col) 
    - 主键索引，不允许空值



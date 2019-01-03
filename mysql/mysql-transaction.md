### mysql事务
#### 事务的特性
1. 原子性：在整个事务中的所有操作，要么全部完成，要么全部不做，没有中间状态。对于事务在执行中发生错误，所有的操作都会被回滚，整个事务就像从没被执行过一样。
2. 一致性：事务执行的结果必须使数据库数据从一个一致性状态变到另一个一致性状态，转账为例，A有500元，B有300元，如果在一个事务里A成功转给B50元，那么不管并发多少，
不管发生什么，只要事务执行成功了，那么最后A账户一定是450元，B账户一定是350元。
3. 隔离性：事务与事务之间不会互相影响，一个事务的中间状态不会被其他事务感知。
4. 持久性：是指一个事务一旦被提交了，那么对数据库中的数据的改变就是永久性的

#### 事务并发会导致的问题？
- 脏读：事务A读取了事务B尚未提交的更新数据，并对数据进行操作，此时事务B回滚，事务A读到的就是错误数据，比如常见的存款和取款<br/>
- 不可重复读: 指事务A读取了事务B已经提交的更改数据。假如A在取款事务的过程中，B往该账户转账100，A两次读取的余额发生不一致
- 幻读: 事务A读取事务B提交的新增数据,会引发幻读问题,比如账户总金额统计：<br/>
    ![幻读](https://github.com/shifefiei/job-interview/blob/master/static/picture/huandu.png)<br/>
    不可重复读和幻读的区别是：前者是指读到了已经提交的事务的 更改数据（修改或删除），后者是指读到了其他已经提交事务的 新增数据
- 丢失更新
1. 事务A撤销时，把事务B已经提交的更新数据覆盖了
    ![更新丢失1](https://github.com/shifefiei/job-interview/blob/master/static/picture/update-1.png)<br/>
2. 事务A覆盖事务B已经提交的数据，造成事务B所做的操作丢失
    ![更新丢失2](https://github.com/shifefiei/job-interview/blob/master/static/picture/update-2.png)<br/>
    
#### 事务隔离级别
- Read Uncommitted
- Read Committed
- Repeatable Read
- Serializable
  ![幻读](https://github.com/shifefiei/job-interview/blob/master/static/picture/gljb.png)<br/>
### spring五个事务隔离级别和七个事务传播行为

#### 五个隔离级别
spring在 TransactionDefinition 接口中定义了五个不同的事务隔离级别<br/>

1. ISOLATION_DEFAULT 使用数据库默认的事务隔离级别
2. ISOLATION_READ_UNCOMMITTED <br/>
    事务最低的隔离级别，它充许别外一个事务可以看到这个事务未提交的数据。这种隔离级别会产生脏读，不可重复读和幻像读
3. ISOLATION_READ_COMMITTED <br/>
    保证一个事务修改的数据提交后才能被另外一个事务读取。不允许脏读的出现，可能会出现不可重复读和幻像读。
4. ISOLATION_REPEATABLE_READ  <br/>
    这种事务隔离级别可能会出现幻像读。
6. ISOLATION_SERIALIZABLE  <br/>
    最可靠的事务隔离级别。事务被处理为顺序执行。除了防止脏读，不可重复读外，还避免了幻像读。


#### 七个事务传播行为

在TransactionDefinition接口中定义了七个事务传播行为: <br/>

1. PROPAGATION_REQUIRED 如果存在一个事务，则支持当前事务。如果没有事务则开启一个新的事务。默认的spring事务传播级别。

2. PROPAGATION_SUPPORTS 如果存在一个事务，支持当前事务。如果没有事务，则非事务的执行。但是对于事务同步的事务管理器，PROPAGATION_SUPPORTS与不使用事务有少许不同。

3. PROPAGATION_MANDATORY 如果已经存在一个事务，支持当前事务。如果没有一个活动的事务，则抛出异常。

4. PROPAGATION_REQUIRES_NEW 总是开启一个新的事务。如果一个事务已经存在，则将这个存在的事务挂起。

5. PROPAGATION_NOT_SUPPORTED 总是非事务地执行，并挂起任何存在的事务。

6. PROPAGATION_NEVER 总是非事务地执行，如果存在一个活动事务，则抛出异常

7. PROPAGATION_NESTED 如果一个活动的事务存在，则运行在一个嵌套的事务中. 如果没有活动事务, 则按TransactionDefinition.PROPAGATION_REQUIRED 属性执行

#### [参考文章](https://yq.aliyun.com/articles/48893)
####[spring事务传播属性](https://blog.csdn.net/weixin_39625809/article/details/80707695)
####[事务传播中的方法调用](https://blog.csdn.net/KinseyGeek/article/details/54931710)
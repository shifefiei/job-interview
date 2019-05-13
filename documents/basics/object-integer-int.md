### Object 类中有哪些方法

- equals(Object obj) 
- hashCode() 
- toString() 

- clone() : 创建并返回此对象的一个副本。
- finalize() : 当垃圾回收器确定不存在对该对象的更多引用时，由对象的垃圾回收器调用此方法
- getClass() 

- notify() 
- notifyAll() 
- wait() 

### int 和 Integer 的区别
- Integer是int的包装类，int则是java的一种基本数据类型 
- Integer变量必须实例化后才能使用，而int变量不需要 
- Integer实际是对象的引用，当new一个Integer时，实际上是生成一个指针指向此对象；而int则是直接存储数据值 
- Integer的默认值是null，int的默认值是0

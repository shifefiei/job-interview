### CAS 算法

1. 什么是CAS？<br/>
(1) Compare and Swap，即比较再交换。<br/>
(2) CAS需要有3个操作数：内存地址V，旧的预期值A，即将要更新的目标值B。<br/>
(3) CAS指令执行时，当且仅当内存地址V的值与预期值A相等时，将内存地址V的值修改为B，否则就什么都不做。整个比较并替换的操作是一个原子操作。


```java
class AtomicInteger{

    /**
     * 原子自增 
     */
    public final int incrementAndGet() {
        return unsafe.getAndAddInt(this, valueOffset, 1) + 1;
    }
}


class Unsafe {
    
    /**
    * 拿到内存位置的最新值 var5，使用CAS尝试修将内存位置的值修改为目标值 var5 + var4，
    * 如果修改失败，则会继续循环获取该内存位置的新值v，然后继续尝试，直至修改成功。
    */
    public final int getAndAddInt(Object var1, long var2, int var4) {
        int var5;
        do {
            var5 = this.getIntVolatile(var1, var2);
        } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));

        return var5;
     }
}
```

2. CAS存在的问题 <br/>
(1) 长时间循环导致系统开销大 <br/>
(2) ABA问题：<br/>
    如果内存地址V初次读取的值是A，并且在准备赋值的时候检查到它的值仍然为A，那我们就能说它的值没有被其他线程改变过了吗？<br/>
    如果在这段期间它的值曾经被改成了B，后来又被改回为A，那CAS操作就会误认为它从来没有被改变过。这个漏洞称为CAS操作的“ABA”问题

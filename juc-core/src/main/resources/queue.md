# 队列
## Queue
用一元数组保存队列，遵守先进先出原则

## Deque
双端队列，两端都可以进出元素，当我们约定从一端进出元素时，就是我们的栈结构(后进先出)

## BlockingQueue
阻塞队列
### ArrayBlockingQueue 
素组阻塞队列，是一个由数组支持的有界阻塞队列，次队列按照先进先出原则操作元素
1. 固定大小的数组，容量达到最大值时，插入新元素就会阻塞，尝试从空队列中取元素也会阻塞

### DelayQueue 

### LinkedBlockingDeque

### LinkedBlockingQueue
链表阻塞队列
1. 有链表构成的缓冲队列
2. 如果不指定初始化容量默认 Integer.MAX_VALUE 

### LinkedTransferQueue 

### PriorityBlockingQueue 


### SynchronousQueue

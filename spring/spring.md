### spring aop的原理和实现
#### [代理模式](https://github.com/shifefiei/dp-review/blob/master/dp-proxy/src/main/resources/proxy.md)
#### spring aop
1. 什么是 spring aop?
spring aop 都知道是面向切面编程，主要的实现手段有 spring-aop 和 AspectJ

- spring aop 
spring aop 采用的是动态代理模式实现的，即利用jdk动态代理和cglib动态代理<br/>
(1) jdk代理的实现利用了java.lang.reflect包的Proxy类的newProxyInstance方法 以及 InvocationHandler 接口来调用具体方法<br/>
(2) cglib 依赖spring-core核心包，是一个代码生成类库，可以在运行时动态的生成某个类的子类，cglib代理要求类和方法没有被final关键字修饰<br/>

spring aop默认使用的是 jdk 动态代理，如果代理的类没有实现接口则会使用cglib代理 
- spring aop的通知类型<br/>
(1) Before : 前置通知，目标方法调用之前执行<br/>
(2) After : 后置通知，目标方法完成之后执行，不关心方法结果<br/>
(3) After-returning : 返回通知，目标方法成功执行之后通知<br/>
(4) After-throwing : 异常通知，目标方法抛出异常后通知<br/>
(5) Around : 环绕通知，方法调用之前和之后执行<br/>


#### spring ioc的原理和实现
- ioc 和 di 的区别<br/>
ioc 控制反转，指将对象的创建权，反转到spring容器 ; di 依赖注入，指spring创建对象的过程中，将对象依赖属性通过配置进行注入

- ioc 原理<br/>
我理解的ioc容器主要功能就是对 spring bean 的管理的过程，spring ioc 的核心组件：
1. BeanFactory : 定义了一个简单的 ioc 容器，spring 在 BeanFactory的基础上可以扩展，主要是管理bean的加载、实例化等
2. ApplicationContext ：对于 ApplicationContext 而言，则扩展了BeanFactory ,提供了更多的额外功能，比如：ClassPathXmlApplicationContext
3. BeanDefinition ：它定义了每个 bean 的信息，比如 bean 的属性，类名，类型等的信息

- bean 的生命周期
1. spring 对 bean 初始化
2. spring bean 属性填充
3. 调用BeanNameAware接口的setBeanName()方法，如果实现了该接口
4. 调用BeanFactoryAware接口的setBeanFactory()方法，如果实现了该接口
5. 调用ApplicationContextAware接口的setApplicationContext()方法
6. 调用BeanPostProcessor接口的postProcessBeforeInitialization()方法
7. 调用InitializingBean接口的afterPropertiesSet()方法
8. 调用BeanPostProcessor接口的postProcessAfterInitialization()方法
9. bean准备就绪
10. 调用DisposableBean接口的destroy方法

#### spring bean实例化的方式有哪些
- 使用类的构造器
- 简单工厂模式
- 工厂方法模式实例化，先创建工厂实例beanFactory，再通过工厂实例创建目标bean实例

#### spring bean注入属性的方式 
- 接口注入
- 构造器注入
- setter方法注入

#### spring 中用到了那些设计模式
- [工厂方法模式](https://github.com/shifefiei/dp-review/blob/master/dp-factory/src/main/resources/factory.md)，比如各种 BeanFactory
- [单例模式](https://github.com/shifefiei/dp-review/blob/master/dp-singleton/src/main/resources/singleton.md)，spring 默认单例模式
- [策略模式](https://github.com/shifefiei/dp-review/blob/master/dp-strategy/src/main/resources/strategy.md)，例如：当bean需要访问资源配置文件时就会用到策略模式。Resource 接口封装了各种可能的资源类型，
包括了：UrlResource，ClassPathResource，FileSystemResource等，Spring需要针对不同的资源采取不同的访问策略。
在这里，Spring让ApplicationContext成为了资源访问策略的“决策者”
- [代理模式](https://github.com/shifefiei/dp-review/blob/master/dp-proxy/src/main/resources/proxy.md)

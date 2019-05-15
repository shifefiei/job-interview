package com.sff.juc.pool;

import com.netflix.hystrix.*;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 使用 Hystrix 来隔离线程池
 */
public class ThreadPoolIsolationDemo {

    /**
     * 订单服务
     */
    static class CommandOrder extends HystrixCommand<String> {


        private String orderName;

        public CommandOrder(String orderName) {

            super(Setter.withGroupKey(
                    //服务分组
                    HystrixCommandGroupKey.Factory.asKey("OrderGroup"))
                    //线程分组
                    .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("OrderPool"))
                    .andThreadPoolPropertiesDefaults(
                            HystrixThreadPoolProperties.Setter()
                                    .withCoreSize(2)
                                    .withMaximumSize(10)
                                    .withKeepAliveTimeMinutes(60)
                                    .withMaxQueueSize(20)
                    )
                    .andCommandPropertiesDefaults(
                            HystrixCommandProperties.Setter()
                                    //线程池隔离
                                    .withExecutionIsolationStrategy(
                                            HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                    )
            );
            this.orderName = orderName;

        }

        @Override
        protected String run() throws Exception {
            System.out.println("<<<<<<<<<<<<<<<<<<< " + orderName + " >>>>>>>>>>>>>>>>>>>");
            TimeUnit.MILLISECONDS.sleep(100);
            return "orderName = " + orderName;
        }
    }


    /**
     * 用户服务
     */
    static class CommandUser extends HystrixCommand<String> {


        private String userName;

        public CommandUser(String userName) {

            super(Setter.withGroupKey(
                    HystrixCommandGroupKey.Factory.asKey("UserGroup"))
                    .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("UserPool"))
                    .andThreadPoolPropertiesDefaults(
                            HystrixThreadPoolProperties.Setter()
                                    .withCoreSize(2)
                                    .withMaximumSize(10)
                                    .withKeepAliveTimeMinutes(60)
                                    .withMaxQueueSize(20)
                    )
                    .andCommandPropertiesDefaults(
                            HystrixCommandProperties.Setter()
                                    .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                    )
            );
            this.userName = userName;

        }

        @Override
        protected String run() throws Exception {
            System.out.println(">>>>>>>>>>>>>>>>>>> " + userName + " <<<<<<<<<<<<<<<<<<<<");
            TimeUnit.MILLISECONDS.sleep(100);
            return "orderName = " + userName;
        }
    }


    public static void main(String[] args) throws Exception {

        CommandOrder commandPhone = new CommandOrder("手机");
        CommandOrder commandPc = new CommandOrder("电视");
        //阻塞方式执行
        String execute = commandPhone.execute();
        System.out.println(" phone : " + execute);

        //异步非阻塞方式
        Future<String> queue = commandPc.queue();
        String value = queue.get(200, TimeUnit.MILLISECONDS);
        System.out.println("pc : " + value);


        CommandUser commandUser = new CommandUser("张三");
        String name = commandUser.execute();
        System.out.println("name : " + name);

    }

}

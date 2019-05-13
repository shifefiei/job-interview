package com.sff.dp.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author shifeifei
 * @date 2019-05-13 10:04
 * <p>
 * jdk动态代理
 */
public class JdkProxyFactory {

    private Object target;

    public JdkProxyFactory(Object target) {
        this.target = target;
    }

    public Object getTarget() {
        Object object = Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),

                //lambda中无法直接对异常进行处理，需要进行单独封装处理
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args)
                            throws Throwable {
                        return method.invoke(target, args);
                    }
                }
        );
        return object;
    }

}

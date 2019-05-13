package com.sff.dp.proxy.cglib;

/**
 * @author shifeifei
 * @date 2019-05-13 10:26
 * <p>
 * cglib 代理的对象没有实现任何接口
 */
public class CglibTicket {

    public void buy() {
        System.out.println("I want to buy ticket");
    }

}

package com.sff;

import com.sff.dp.proxy.BuyTrainTicket;
import com.sff.dp.proxy.BuyTrainTicketOn12306;
import com.sff.dp.proxy.JdkProxyFactory;
import com.sff.dp.proxy.QunarProxyBuyTrainTicket;
import com.sff.dp.proxy.cglib.CglibProxyFactory;
import com.sff.dp.proxy.cglib.CglibTicket;

/**
 * @author shifeifei
 * @date 2019-05-13 10:51
 */
public class ProxyTest {


    public static void main(String[] args) {
        stillProxy();
        jdkProxy();
        cglibProxy();
    }


    /**
     * 静态代理测试
     */
    public static void stillProxy() {
        BuyTrainTicketOn12306 on12306 = new BuyTrainTicketOn12306();
        QunarProxyBuyTrainTicket qunarProxyBuyTrainTicket = new QunarProxyBuyTrainTicket(on12306);
        qunarProxyBuyTrainTicket.buy();
    }

    /**
     * jdk代理测试
     */
    public static void jdkProxy() {
        BuyTrainTicketOn12306 buy = new BuyTrainTicketOn12306();
        BuyTrainTicket trainTicket = (BuyTrainTicket) new JdkProxyFactory(buy).getTarget();
        trainTicket.buy();
    }

    /**
     * cglib代理测试
     */
    public static void cglibProxy() {
        CglibTicket trainTicket = new CglibTicket();
        CglibTicket ticketProxy = (CglibTicket) new CglibProxyFactory(trainTicket).getTarget();
        ticketProxy.buy();
    }
}

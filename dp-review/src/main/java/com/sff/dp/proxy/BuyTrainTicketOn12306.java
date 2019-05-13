package com.sff.dp.proxy;

/**
 * @author shifeifei
 * @date 2019-05-13 09:58
 * <p>
 * 真实购买火车票对象
 */
public class BuyTrainTicketOn12306 implements BuyTrainTicket {

    public void buy() {
        System.out.println("12306买票成功");
    }
}

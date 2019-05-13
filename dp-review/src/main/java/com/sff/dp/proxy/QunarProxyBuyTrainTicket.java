package com.sff.dp.proxy;

/**
 * @author shifeifei
 * @date 2019-05-13 10:57
 */
public class QunarProxyBuyTrainTicket implements BuyTrainTicket {

    private BuyTrainTicket buyTrainTicket;

    public QunarProxyBuyTrainTicket(BuyTrainTicket buyTrainTicket) {
        this.buyTrainTicket = buyTrainTicket;
    }

    public void buy() {
        System.out.println("-------进入去哪儿平台-------");
        buyTrainTicket.buy();
        System.out.println("-------退出去哪儿平台-------");
    }

}

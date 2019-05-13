package com.sff.dp.strategy;

/**
 * @author shifeifei
 * @date 2019-05-13 11:10
 */
public class RouteStrategy {

    private MemberCard memberCard;

    public RouteStrategy(MemberCard memberCard) {
        this.memberCard = memberCard;
    }

    public void route() {
        memberCard.buy();
    }

}

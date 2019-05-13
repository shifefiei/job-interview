package com.sff;

import com.sff.dp.strategy.MemberCard;
import com.sff.dp.strategy.MonthMemberCardStrategy;
import com.sff.dp.strategy.RouteStrategy;
import com.sff.dp.strategy.YearMemberCardStrategy;

/**
 * @author shifeifei
 * @date 2019-05-13 11:11
 */
public class StrategyTest {

    public static void main(String[] args) {
        //策略1
        MemberCard yearMemberCard = new YearMemberCardStrategy();
        RouteStrategy routeStrategy = new RouteStrategy(yearMemberCard);
        routeStrategy.route();


        //策略2
        MemberCard monthCardMember = new MonthMemberCardStrategy();
        routeStrategy = new RouteStrategy(monthCardMember);
        routeStrategy.route();
    }
}

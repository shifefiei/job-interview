package com.sff.dp.strategy;

/**
 * @author shifeifei
 * @date 2019-05-13 11:07
 * <p>
 * 月卡购买策略
 */
public class MonthMemberCardStrategy extends MemberCard {

    @Override
    public void buy() {
        System.out.println("购买月卡");
    }
}

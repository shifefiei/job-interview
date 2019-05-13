package com.sff.dp.strategy;

/**
 * @author shifeifei
 * @date 2019-05-13 11:06
 * <p>
 * 年卡购买策略
 */
public class YearMemberCardStrategy extends MemberCard {

    @Override
    public void buy() {
        System.out.println("购买年卡");
    }
}

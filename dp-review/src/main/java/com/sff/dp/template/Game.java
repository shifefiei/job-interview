package com.sff.dp.template;

/**
 * @author shifeifei
 * @date 2019-05-13 12:03
 * <p>
 * <p>
 * 优点：
 * 1、封装不变部分，扩展可变部分
 * 2、提取公共代码，便于维护
 * 3、行为由父类控制，子类实现
 * <p>
 * 缺点：每一个不同的实现都需要一个子类来实现，导致类的个数增加，使得系统更加庞大
 */
public abstract class Game {

    abstract public void init();

    abstract public void start();

    abstract public void end();

    /**
     * 玩游戏的模板方法：预留方法有子类去实现
     */
    public void play() {
        init();
        start();
        end();
    }

}

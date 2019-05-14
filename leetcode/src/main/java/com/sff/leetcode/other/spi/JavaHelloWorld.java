package com.sff.leetcode.other.spi;

public class JavaHelloWorld implements HelloWorld {
    @Override
    public void sayHello() {
        System.out.println("Java,hello");
    }
}

package com.sff.leetcode.other.spi;

public class PythonHelloWorld implements HelloWorld {
    @Override
    public void sayHello() {
        System.out.println("Python,hello");
    }
}

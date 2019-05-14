package com.sff.leetcode.other.spi;

import java.util.ServiceLoader;

public class TestJavaSpi {

    public static void main(String[] args) {

        ServiceLoader<HelloWorld> serviceLoader = ServiceLoader.load(HelloWorld.class);

        serviceLoader.forEach(HelloWorld::sayHello);

        System.out.println("-----------");

        serviceLoader.forEach((HelloWorld hello) -> hello.sayHello());
    }

}

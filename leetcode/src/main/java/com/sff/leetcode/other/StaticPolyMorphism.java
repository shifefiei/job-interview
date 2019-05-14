package com.sff.leetcode.other;

class StaticSuper {

    public static String staticGet() {
        return "Base staticGet()";
    }

    public String dynamicGet() {
        return "Base dynamicGet()";
    }
}

class StaticSub extends StaticSuper {

    public static String staticGet() {
        return "Derived staticGet()";
    }

    @Override
    public String dynamicGet() {
        return "Derived dynamicGet()";
    }
}

public class StaticPolyMorphism {

    public static void main(String[] args) {



        byte a = 127;
        byte j = 2;

        System.out.println("======="+a+j);

        int b = Integer.MAX_VALUE + 1;
        System.out.println(Math.addExact(Integer.MAX_VALUE,1));

//        List<Integer> numbers = Arrays.asList(1, 3, 3, 4, 5, 6);
//        numbers.stream()
//                .filter((Integer i) -> i % 2 == 0)
//                .forEach(
//                        (Integer s) -> add(s)
//                );
//
//
//        System.out.println("------------");
//        StaticSuper sup = new StaticSub();
//
//        System.out.println(sup.staticGet());
//        System.out.println(sup.dynamicGet());

    }

    private static void add(Integer s) {
        int a = 0;
        a = a + s;
        System.out.println(a+"++++++");
    }
}
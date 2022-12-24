package com.github.kettoleon.ti.dashboard;

import java.util.function.Supplier;

public class ProfileUtils {

    public static <R> R measure(String name, Supplier<R> method) {
        System.out.println(name + " starting...");
        long start = System.currentTimeMillis();
        R ret = method.get();
        long duration = System.currentTimeMillis() - start;
        System.out.println(name + " took: " + duration + "ms");
        return ret;
    }

    public static void measure(String name, Runnable method) {
//        System.out.println(name + " starting...");
        long start = System.currentTimeMillis();
        method.run();
        long duration = System.currentTimeMillis() - start;
//        System.out.println(name + " took: " + duration + "ms");
    }

}

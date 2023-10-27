package com.solvd.laba.homework01.exercise01;


public class Hello {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please, provide arguments for printing.");
        } else {
            for (String arg : args) {
                System.out.println(arg);
            }
        }
    }
}

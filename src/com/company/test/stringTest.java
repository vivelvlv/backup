package com.company.test;

/**
 * Created by vive on 16/5/22.
 */
public class stringTest {
    public static void main(String[] args) {
        String temp = "hello";
        stringtt(temp);
        System.out.println(temp);
    }

    public static void stringtt(String string) {
        string += "ddd";
        System.out.println(string);
    }

}

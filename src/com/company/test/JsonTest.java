package com.company.test;

import net.sf.json.JSONObject;

/**
 * Created by vive on 16/5/23.
 */
public class JsonTest {

    public static void main(String[] a) {
        JSONObject dd = JSONObject.fromString("{\"dd\":\"ee\"}");
        System.out.println(dd);
        dd.remove("dd");
        dd.put("dd","cc");
        System.out.println(dd);
    }
}

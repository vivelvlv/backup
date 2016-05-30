package com.company.xt.utils;

/**
 * Created by vive on 16/5/23.
 */
public class TextUtil {

    public static boolean isEmpty(String content) {
        if (content == null || content.equals("")) {
            return true;
        }
        return false;
    }
}

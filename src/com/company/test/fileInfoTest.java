package com.company.test;

import com.company.xt.utils.FileImpl;

import java.io.File;

/**
 * Created by vive on 16/5/31.
 */
public class fileInfoTest {
    public static void main(String[] args) {
        FileImpl fileUtil = new FileImpl();
        File file = new File("/Users/vive/Desktop/backup/src/com/company/");
        //
        System.out.println(fileUtil.dirInfo("/Users/vive/Desktop/backup/src/com/company/"));
    }
}

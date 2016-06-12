package com.company.test;

import com.company.xt.utils.FileImpl;

import java.io.File;

/**
 * Created by vive on 16/5/31.
 */
public class writeImageTest {
    public static void main(String[] args) {
        FileImpl fileUtil = new FileImpl();
        File file = new File("/Users/vive/Downloads/ddd_jpeg_test.bin");
        fileUtil.writeFile(file, "v".getBytes(), true);
    }
}

package com.company.test;

import com.company.xt.utils.FileImpl;
import com.company.xt.utils.FileUtil;

import java.io.File;

/**
 * Created by vive on 16/6/15.
 */
public class lnkTest {
    public static void main(String[] args) {
        FileUtil fileUtil = new FileImpl();
        File realFile = new File("/Users/vive/Desktop/Mou.app");
        File lnkFile = new File("/Users/vive/Desktop/Mou.app/Contents/Frameworks/Sparkle.framework/Headers");

        fileUtil.copyFile(lnkFile,new File("/Users/vive/Desktop/dex-test/kkkk"),false);
        System.out.println("");

    }
}

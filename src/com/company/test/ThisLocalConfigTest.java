package com.company.test;

import com.company.xt.serverinfo.ThisLocalConfig;

/**
 * Created by vive on 16/6/5.
 */
public class ThisLocalConfigTest {


    public static void main(String[] args) {
        ThisLocalConfig thisLocalConfig = new ThisLocalConfig("ThisLocalConfigTest", "/Users/vive/Desktop/dex-test");
        System.out.println(thisLocalConfig.getBackupDirList());
        System.out.println(thisLocalConfig.getBeginTime());
        System.out.println(thisLocalConfig.getIntval());
    }
}

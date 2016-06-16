package com.company.test;

import com.company.xt.utils.FileImpl;
import com.company.xt.utils.FileUtil;

import java.io.File;
import java.util.List;

/**
 * Created by vive on 16/6/12.
 */
public class testDirInfoTest {
    public static void main(String[] args) {
        FileUtil fileUtil = new FileImpl();
        List<String[]> tempList = fileUtil.dirInfo("/Users/vive/Desktop/mac-from/额外工作上的事情", null, new FileUtil.Operate() {
            @Override
            public boolean operate(File file, String[] info) {
                System.out.println("扫描结果: " + info[0] + "," + info[1] + "," + info[2] + "," + info[3] + "," + info[4]);
                String path = file.getAbsolutePath();
                fileUtil.copyFile(path, fileUtil.calcRomatePath("haha", "/Users/vive/Desktop/dex-test", "/Users/vive/Desktop/mac-from/额外工作上的事情", path), false);
                return false;
            }
        });
        System.out.println("----------------------------------------");

        List<String[]> temp = fileUtil.dirInfo("/Users/vive/Desktop/mac-from/额外工作上的事情", tempList, new FileUtil.Operate() {
            @Override
            public boolean operate(File file, String[] info) {
                System.out.println("扫描结果: " + info[0] + "," + info[1] + "," + info[2] + "," + info[3] + "," + info[4]);
                String path = file.getAbsolutePath();
                fileUtil.copyFile(path, fileUtil.calcRomatePath("haha", "/Users/vive/Desktop/dex-test", "/Users/vive/Desktop/mac-from/额外工作上的事情", path), false);
                return false;
            }
        });


    }
}

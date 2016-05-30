package com.company.test;

import java.io.File;

/**
 * Created by baidu on 16/5/9.
 */
public class createNewFileTest {
    public static void main(String[] args) {

    }

    private void log(String content) {
        System.out.println(content);
    }

    private File getPath() {
        //取得根目录路径
        String rootPath = getClass().getResource("/").getFile().toString();
        //当前目录路径
        String currentPath1 = getClass().getResource(".").getFile().toString();
        String currentPath2 = getClass().getResource("").getFile().toString();
        //当前目录的上级目录路径
        String parentPath = getClass().getResource("../").getFile().toString();

        try {
            File iml = new File(rootPath);
            if (iml.isDirectory()) {
                return iml;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createFile(File file) {
        
    }
}

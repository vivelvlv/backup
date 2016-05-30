package com.company.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by baidu on 16/5/9.
 */
public class readContentTest {
    public static void main(String[] args) {
        readContentTest read = new readContentTest();
        while (true) {
            try {
                Thread.sleep(999);
            } catch (Exception e) {
                break;
            }
            read.readFile(read.getFile());
        }
    }

    private void log(String content) {
        System.out.println(content);
    }

    private File getFile() {
        //取得根目录路径
        String rootPath = getClass().getResource("/").getFile().toString();
        //当前目录路径
        String currentPath1 = getClass().getResource(".").getFile().toString();
        String currentPath2 = getClass().getResource("").getFile().toString();
        //当前目录的上级目录路径
        String parentPath = getClass().getResource("../").getFile().toString();

        try {
            File iml = new File(rootPath);
            log("get iml file : getName = " + iml.getName() + ",getPath = " + iml.getPath());
            return iml;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void readFile(File file) {
        BufferedReader reader = null;
        try {
            log("以行为单位读取文件内容,一次读取一行");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {
                log("line " + line + ": " + tempString);
                line++;
            }
            try {
                Thread.sleep(27);
            } catch (Exception e) {
                e.printStackTrace();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}

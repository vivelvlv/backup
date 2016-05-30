package com.company.test;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by baidu on 16/5/9.
 */
public class writeContentTest {
    int i = 0;

    public static void main(String[] args) {
        writeContentTest write = new writeContentTest();
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                break;
            }
            write.writeFile(write.getFile());
        }
    }

    private String genContent() {
        return "this is number " + i++ + "'s item";
    }

    private static void log(String content) {
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
            File iml = new File(rootPath + "1.txt");
            return iml;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void writeFile(File file) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            String content = genContent();
            out.write(content.getBytes());
            log("write content:" + content + ",and then sleep 100");
            try {
                Thread.sleep(100);
            } catch (Exception e) {

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

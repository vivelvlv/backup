package com.company.test;


import com.company.xt.utils.FileImpl;

/**
 * Created by vive on 16/5/22.
 */
public class testFileImplFileModel {

    public static void log(String content) {
        System.out.println(content);
    }

    public static void log(boolean content) {
        log(content + "");
    }

    public static void log(int content) {
        log(content + "");
    }

    public static void log(long content) {
        log(content + "");
    }

    public static void main(String[] args) {
        FileImpl test = new FileImpl();
//
//        // createfile
//        String src = "/Users/vive/Desktop/BackUpFileForXT/src/com/company/test/TestResultDir";
//        log(test.createFile(src + File.separator + "crestFile") + "");
//
//        // isFileExist
//        log(test.isFileExist(src + "/crestFile") + "");
//        log(test.isFileExist(src + "/creatFile") + "");
//
//        // deleteFile
//        log(test.deleteFile(src + "/crestFile"));
//
//        // writeFile or append
//        log(test.createFile(src + File.separator + "createFile.txt") + "");
//        log(test.writeFile(new File(src + File.separator + "createFile.txt"), "hello".getBytes(), false));
//        log(test.writeFile(new File(src + File.separator + "createFile.txt"), "fairy".getBytes(), false));
//        log(test.writeFile(new File(src + File.separator + "createFile.txt"), ",nice to meet you".getBytes(), true));
//        log(test.writeFile(new File(src + File.separator + "createFile.txt"), "\nnice to meet you-".getBytes(), true));
//
//        // readFile
//        log(test.readFile(src + File.separator + "createFile.txt"));
//
//
//        // copyFile
//        log("copyFile:" + test.copyFile(src + File.separator + "createFile.txt", src + File.separator + "decDir" + File.separator + "createFile.txt", true));
//
//        // getFileLastChangeTime
//
//        long lastTime = test.getFileLastChangeTime(src + File.separator + "createFile.txt");
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String timeformat = sdf.format(new Date(lastTime));
//        log("getFileLastChangeTime: " + timeformat);
//
//        // getFileMD5
//        log("MD5: " + test.getFileMD5(src + File.separator + "createFile.txt"));
//
//        // isFileSame
//        String baseAddr = src + File.separator;
//        String compare1 = baseAddr + "compare1.txt";
//        String compare2 = baseAddr + "compare2.txt";
//
//        test.createFile(compare1);
//        test.createFile(compare2);
//
//        test.writeFile(new File(compare1), "hello compare-".getBytes(), false);
//        test.writeFile(new File(compare2), "hello compare-".getBytes(), false);
//
//
//        log("is file same : " + test.isFileSame(compare1, compare2));
//
//        log("------------------- in dir model -------------------");
//
//        // create dir
//        log("create dir: " + test.createDir(baseAddr + "createDir"));
//
//        // isDirExist
//        log("isDirex: " + test.isDirExist(baseAddr + "createDir"));
//
//        // deleteDir
//        log("deleteDir : " + test.deleteDir(baseAddr + "createDir"));
//
//        // isDirExist
//        log("isDir: " + test.isDirExist(baseAddr + "createDir"));
//
//        // fileInDir
//        File[] files = test.fileInDir(baseAddr);
//        for (int i = 0; i < files.length; i++) {
//            File file = files[i];
//            String type = null;
//            if (file.isDirectory()) {
//                type = "dir";
//            } else {
//                type = "file";
//            }
//            log("this is file or dir :" + type + ",name : " + file.getName());
//        }

        // copyDir
        try {
            test.copyDir("/Users/vive/Desktop/downLoad/markdown/",
                    "/Users/vive/Desktop/" + "eeeee");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

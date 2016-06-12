package com.company.xt.utils;

import jdk.nashorn.api.scripting.JSObject;
import net.sf.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by vive on 16/5/9.
 */
public interface FileUtil {

    public static int CREATE_FILE_SUCCESS = 20;// 创建文件成功
    public static int CREATE_FILE_FAIL = 21;// 创建文件失败
    public static int CREATE_DIR_SUCCESS = 22;// 创建目录成功
    public static int CREATE_DIR_FAIL = 23;// 创建目录失败
    public static int FILE_EXIST = 0;// 文件已存在
    public static int FILE_NO_EXIST = 1;// 文件不存在
    public static int DIR_EXIST = 2;// 目录已存在
    public static int DIR_NO_EXIST = 3;// 目录不存在
    public static int FILE_IS_LASTEST = 4;// 文件是最新的
    public static int FILE_NO_LASTEST = 5;// 文件不是最新的
    public static int DIR_IS_LASTEST = 6;// 目录是最新的
    public static int DIR_NO_LASTEST = 7;// 目录不是最新的
    public static int IS_DIR_NOT_FILE = 8;// 是目录不是文件

    // 创建文件
    public int createFile(String fileName);

    // 文件是否存在
    public boolean isFileExist(String fileName);

    public boolean isFileExist(File file);

    // 删除文件
    public boolean deleteFile(String fileName);

    public boolean deleteFile(File file);

    // 写文件
    public boolean writeFile(String fileName, byte[] content, boolean isAppend);

    public boolean writeFile(File file, byte[] content, boolean isAppend);

    // 读文件
    public String readFile(String fileName);

    public String readFile(File file);

    // 拷贝文件
    public boolean copyFile(String sourceFileName, String descFileName, boolean overlay);

    public boolean copyFile(File sourceFile, File desFileName, boolean overlay);

    // 移动文件
    public boolean moveFile(String sourceFileName, String descFileName);

    // 获得文件上一次更新时间
    public long getFileLastChangeTime(String fileName);

    public long getFileLastChangeTime(File file);

    // 获得文件MD5值
    public String getFileMD5(String fileName);

    public String getFileMD5(File file);

    // 两个文件是否一致
    public boolean isFileSame(String oldFileName, String NewFileName);


    // 生成文件的信息json
    public JSONObject fileInfo(File file);


    ////////////////////////////////////////////////////////


    // 创建目录
    public int createDir(String dirName);

    // 目录是否存在
    public boolean isDirExist(String dirName);

    // 删除目录,同时移除所有子文件以及子目录
    public boolean deleteDir(String dirName);

    // 获得所有目录列表
    public File[] fileInDir(String dirName);

    public File[] fileInDir(File dirFile);

    // 拷贝文件夹
    public boolean copyDir(String srcDir, String desDir) throws FileNotFoundException;


    // 移动文件夹
    public boolean moveDir(String srcDir, String desDir) throws FileNotFoundException;

    // 文件夹的JSON 信息
    public JSONObject dirInfo(String dir);

    // 把本地路径转化成远程server路径
    public String calcRomatePath(String myName, String serverAddr, String myBackUpDir, String currentFilePath);

}

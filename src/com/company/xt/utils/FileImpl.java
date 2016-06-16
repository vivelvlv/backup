package com.company.xt.utils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vive on 16/5/9.
 * 这里的所有的路径都是绝对路径
 */
public class FileImpl implements FileUtil {

    // 打log用的内部方法
    private void log(String content) {
        System.out.println(content);
    }

    @Override
    public File createFile(String fileName) {
        File temp = new File(fileName);
        if (temp.exists()) {
            return temp;
        }
        if (fileName.endsWith(File.separator)) {
            return null;
        }

        if (!temp.getParentFile().exists()) {
            temp.getParentFile().mkdirs();
        }

        try {
            if (temp.createNewFile()) {
                return temp;
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public boolean isFileExist(String fileName) {
        File temp = new File(fileName);
        return isFileExist(temp);
    }

    @Override
    public boolean isFileExist(File file) {
        if (file.exists()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteFile(String fileName) {
        File file = new File(fileName);
        return deleteFile(file);
    }

    @Override
    public boolean deleteFile(File file) {
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }

    @Override
    public boolean writeFile(String fileName, byte[] content, boolean isAppend) {
        File file = new File(fileName);
        return writeFile(file, content, isAppend);
    }

    @Override
    public boolean writeFile(File file, byte[] content, boolean isAppend) {
        if (!file.exists()) {
            return false;
        }
        if (!isAppend) {
            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                bufferedOutputStream.write(content);
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
                outputStream.close();
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return false;
        } else {
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                long fileLength = randomAccessFile.length();
                randomAccessFile.seek(fileLength);
                randomAccessFile.write(content);
                randomAccessFile.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public String readFile(String fileName) {
        File temp = new File(fileName);
        return readFile(temp);
    }

    /**
     * CR ok
     *
     * @param file
     * @return
     */
    @Override
    public String readFile(File file) {
        StringBuilder sb = new StringBuilder();
        if (file.exists() && file.isFile()) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(file));
                String tempString = null;
                while ((tempString = br.readLine()) != null) {
                    sb.append(tempString);
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * CR ok
     *
     * @param sourceFileName
     * @param descFileName
     * @param overlay
     * @return
     */
    @Override
    public boolean copyFile(String sourceFileName, String descFileName, boolean overlay) {
        File sourceFile = new File(sourceFileName);
        File descDirTemp = new File(descFileName);
        return copyFile(sourceFile, descDirTemp, overlay);
    }

    /**
     * 原文件,目标文件
     * CR ok
     *
     * @param sourceFile
     * @param descFile
     * @param overlay
     * @return
     */
    @Override
    public boolean copyFile(File sourceFile, File descFile, boolean overlay) {
        try {
            System.out.println("[copy file] " + sourceFile.getCanonicalPath());
        } catch (Exception e) {

        }
        if (!sourceFile.exists() || !sourceFile.isFile()) {
            return false;
        }
        String descName = descFile.getAbsolutePath();// 目标文件路径

        if (descFile.exists() && descFile.isDirectory()) {
            return false;
        }

        if (descFile.exists()) {
            descFile.delete();
            descFile = new File(descName);
        } else {
            if (!descFile.getParentFile().exists()) {
                if (!descFile.getParentFile().mkdirs()) {
                    return false;
                }
            }
        }
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream ouStream = null;
        try {
            inStream = new FileInputStream(sourceFile);
            ouStream = new FileOutputStream(descFile);

            in = inStream.getChannel();
            out = ouStream.getChannel();
            in.transferTo(0, in.size(), out);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inStream.close();
                in.close();
                ouStream.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public boolean moveFile(String sourceFileName, String descFileName) {
        if (copyFile(sourceFileName, descFileName, true)) {
            boolean result = new File(sourceFileName).delete();
            if (result) {
                return true;
            }
        }
        return false;
    }

    @Override
    public long getFileLastChangeTime(String fileName) {
        File temp = new File(fileName);
        return getFileLastChangeTime(temp);
    }

    @Override
    public long getFileLastChangeTime(File file) {
        if (file.exists()) {
            return file.lastModified();
        }
        return 0;
    }

    @Override
    public String getFileMD5(String fileName) {
        File file = new File(fileName);
        return getFileMD5(file);
    }

    @Override
    public String getFileMD5(File file) {
        if (file.exists()) {
            try {
                return MD5FileUtil.getFileMD5String(file);
            } catch (IOException e) {
                return "";
            }
        }
        return "";
    }


    @Override
    public boolean isFileSame(String oldFileName, String newFileName) {
        File oldFile = new File(oldFileName);
        File newFile = new File(newFileName);
        if (oldFile.exists() && oldFile.isFile() && newFile.exists() && newFile.isFile()) {
            String oldMd5 = getFileMD5(oldFile);
            String newMd5 = getFileMD5(newFile);
            if (oldMd5.equals(newMd5)) {
                return true;
            }
        }
        return false;
    }

    List<String[]> cachehistroyConfig;
    Map<String, String[]> cacheMap;

    @Override
    public String[] fileInfo(File file, List<String[]> histroyConfig, Operate callback) {
        String md5 = null;
        long lastTime = getFileLastChangeTime(file);
        long size = file.length();

        String[] resultReturn;

        if (histroyConfig == null) {
            // 直接计算MD5的整个列表
            md5 = getFileMD5(file);
            if (callback != null) {
                resultReturn = new String[]{md5, lastTime + "", size + "", file.getAbsolutePath(), file.getName()};
                callback.operate(file, resultReturn);
            }
        } else {
            if (cachehistroyConfig == histroyConfig) {
                // 表示已经命中缓存
            } else {
                cachehistroyConfig = histroyConfig;
                // 未命中缓存
                cacheMap = new HashMap<>();
                for (String[] item : histroyConfig) {
                    cacheMap.put(item[3], item);
                }

            }

            String[] result = cacheMap.get(file.getAbsolutePath());
            if (result == null) {
                md5 = getFileMD5(file);
                if (callback != null) {
                    resultReturn = new String[]{md5, lastTime + "", size + "", file.getAbsolutePath(), file.getName()};
                    callback.operate(file, resultReturn);
                }
            } else {
                if (result[1].equals(lastTime + "")) {
                    md5 = result[0];
                } else {
                    md5 = getFileMD5(file);
                    if (callback != null) {
                        resultReturn = new String[]{md5, lastTime + "", size + "", file.getAbsolutePath(), file.getName()};
                        callback.operate(file, resultReturn);
                    }
                }
            }

        }
        resultReturn = new String[]{md5, lastTime + "", size + "", file.getAbsolutePath(), file.getName()};
        return resultReturn;
    }


    // about dirs api

    @Override
    public int createDir(String dirName) {
        File dir = new File(dirName);
        if (dir.exists()) {
            return DIR_EXIST;
        }
        if (!dirName.endsWith(File.separator)) {
            dirName += File.separator;
        }
        if (dir.mkdirs()) {
            return CREATE_DIR_SUCCESS;
        } else
            return CREATE_DIR_FAIL;
    }

    @Override
    public boolean isDirExist(String dirName) {
        File dir = new File(dirName);
        if (dir.exists() && dir.isDirectory()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteDir(String dirName) {
        File dir = new File(dirName);
        if (dir.exists() && dir.isDirectory()) {
            return dir.delete();
        }
        return false;
    }

    @Override
    public File[] fileInDir(String dirName) {
        File dir = new File(dirName);
        return fileInDir(dir);
    }

    @Override
    public File[] fileInDir(File dir) {
        if (dir.exists() && dir.isDirectory()) {
            return dir.listFiles();
        }
        return null;
    }

    /**
     * 目录拷贝
     * CR ok
     *
     * @param srcDir 需要拷贝的目录内容
     * @param desDir 目标目录,需要拷贝的目录内容挪到目标目录下
     * @return 成功或失败
     * @throws FileNotFoundException
     */
    @Override
    public boolean copyDir(String srcDir, String desDir) throws FileNotFoundException {
        System.out.println("[copy dir] " + srcDir);
        // 获得拷贝目录的内容
        File src = new File(srcDir);
        if (!src.exists() || !src.isDirectory()) {
            return false;
        }
        File[] srcfiles = fileInDir(src);
        if (srcfiles == null) {
            return false;
        }

        // 创建目标目录
        File dir = new File(desDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!dir.isDirectory()) {
            return false;
        }
        File tempFile = null;
        for (int i = 0; i < srcfiles.length; i++) {
            tempFile = srcfiles[i];
            if (tempFile.isDirectory()) {
                // 轮询到的是子目录,计算目标文件目录下也生成对应的子目录
                String tempDesDir = null;
                if (desDir.endsWith(File.separator)) {
                    tempDesDir = desDir + tempFile.getName();
                } else {
                    tempDesDir = desDir + File.separator + tempFile.getName();
                }
                // 计算原目录下对应的子目录的路径
                String tempSourceDir = null;
                if (srcDir.endsWith(File.separator)) {
                    tempSourceDir = srcDir + tempFile.getName();
                } else {
                    tempSourceDir = srcDir + File.separator + tempFile.getName();
                }
                copyDir(tempSourceDir, tempDesDir);
            } else if (tempFile.isFile()) {
                if (desDir.endsWith(File.separator)) {

                } else {
                    desDir += File.separator;
                }
                copyFile(tempFile, new File(desDir + tempFile.getName()), true);
            }
        }

        return false;
    }

    @Override
    public boolean moveDir(String srcDir, String desDir) throws FileNotFoundException {
        boolean copy = copyDir(srcDir, desDir);
        File des = new File(desDir);
        boolean deleteResult = false;
        if (des.exists() && des.isDirectory()) {
            deleteResult = deleteDir(desDir);
        }
        return copy & deleteResult;
    }

    @Override
    public List<String[]> dirInfo(String dirString, List<String[]> histroyConfig, Operate callback) {
        // System.getProperty("line.separator");
        File dir = new File(dirString);
        List<String[]> tempList = new ArrayList<>();
        if (dir.exists() && dir.isFile()) {
            tempList.add(fileInfo(dir, histroyConfig, callback));
            return tempList;
        }

        if (dir.exists() && dir.isDirectory()) {
            dirInfoLooper(dir, tempList, histroyConfig, callback);
            return tempList;
        }
        return null;
    }


    private List<String[]> dirInfoLooper(File dir, List<String[]> contentList, List<String[]> histroyConfig, Operate callback) {
        if (dir.exists() && dir.isFile()) {
            contentList.add(fileInfo(dir, histroyConfig, callback));
            return contentList;
        }
        if (dir.exists() && dir.isDirectory()) {
            File[] subFiles = dir.listFiles();
            for (File file : subFiles) {
                if (file.isDirectory()) {
                    dirInfoLooper(file, contentList, histroyConfig, callback);
                } else {
                    contentList.add(fileInfo(file, histroyConfig, callback));
                }
            }
        }
        return contentList;
    }

    /**
     * @param myName          本地客户端名字
     * @param serverAddr      在服务器上的根目录
     * @param myBackUpDir     本地文件所在的根存储目录
     * @param currentFilePath 本地文件的所在目录
     * @return
     */
    public String calcRomatePath(String myName, String serverAddr, String myBackUpDir, String currentFilePath) {
        // 在server上的位置: serverAddr + myName + myBackUpDirName + 相对的文件path
        // 相对的文件path = currentFilePath - myBackUpDir

        // 获得相对path
        String myTempBackUpDir = myBackUpDir;
        if (!myBackUpDir.endsWith(File.separator)) {
            myTempBackUpDir += File.separator;
        }
        int length = myTempBackUpDir.length();

        // 获得相对路径下的短名称,最顶部没有文件路径分隔符
        String shortPath = currentFilePath.substring(length);

        String remotePath = null;
        if (!serverAddr.endsWith(File.separator)) {
            remotePath = serverAddr + File.separator + myName + File.separator;
        } else {
            remotePath = serverAddr + myName + File.separator;
        }

        String tempMyBackUpDirName = new File(myBackUpDir).getName();

        //增加我的备份文件夹的路径和相对路径名
        remotePath += tempMyBackUpDirName + File.separator + shortPath;


        return remotePath;
    }

}

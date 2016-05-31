package com.company.xt.utils;

import net.sf.json.JSONObject;

import java.io.*;
import java.nio.channels.FileChannel;

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
    public int createFile(String fileName) {
        File temp = new File(fileName);
        if (temp.exists()) {
            return FILE_EXIST;
        }
        if (fileName.endsWith(File.separator)) {
            return IS_DIR_NOT_FILE;
        }

        if (!temp.getParentFile().exists()) {
            temp.getParentFile().mkdirs();
        }

        try {
            if (temp.createNewFile()) {
                return CREATE_FILE_SUCCESS;
            } else {
                return CREATE_FILE_FAIL;
            }
        } catch (IOException e) {
            return CREATE_FILE_FAIL;
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

    @Override
    public JSONObject fileInfo(File file) {
        String md5 = getFileMD5(file);
        long lastTime = getFileLastChangeTime(file);
        long size = file.length();
        JSONObject object = JSONObject.fromString("{\"lasttime\":" + lastTime
                + ",\"md5\":\"" + md5 + "\",\"size\":" + size + ",\"allpath\":\"" + file.getAbsolutePath() + "\"}");
        return object;
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
    public JSONObject dirInfo(String dirString) {
        File dir = new File(dirString);
        JSONObject dirJson = new JSONObject();

        if (dir.exists() && dir.isFile()) {
            return fileInfo(dir);
        }

        if (dir.exists() && dir.isDirectory()) {
            File[] subFiles = dir.listFiles();
            for (File file : subFiles) {
                if (file.isDirectory()) {
                    dirJson.put("isDirectory", file.getAbsolutePath());
                    if (!dirString.endsWith(File.separator)) {
                        dirString += File.separator;
                    }
                    dirJson.put(file.getName(), dirInfo(dirString + file.getName() + File.separator));
                } else {
                    dirJson.put(file.getName(), fileInfo(file));
                }
            }

            return dirJson;
        }
        return null;
    }


}

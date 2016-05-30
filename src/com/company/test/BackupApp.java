package com.company.test;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class BackupApp {

    private StringBuffer strBuf = null;
    private static StringBuffer fileNameBuf = new StringBuffer();
    private Properties prop = new Properties();

    static final int buffer = 2048;

    private static void print(String content) {
        System.out.println(content);
    }

    public static void main(String[] args) {
        // write your code here
        BackupApp app = new BackupApp();
        print("start ...");
        print("start delete the dst folder ...");
        app.delete();// 删除目标的文件夹内容
        print("create the dst folder ...");
        app.mkdir();
        print("copy src folder to dst folder ...");
        app.copy();
        print("compress dst folder ...");
        app.compress();
        print(fileNameBuf.toString());
        print("END!");
    }

    // 从属性文件中拿到目标拷贝路径和zipdir路径String,然后创建两个文件夹
    private void mkdir() {
        try {
            InputStream is = getClass()
                    .getResourceAsStream("/config.properties");
            prop.load(is);
            String dest = prop.getProperty("dst");
            String zipdir = prop.getProperty("zipdir");
            File f = new File(dest);
            File zipFile = new File(zipdir);
            if (!f.exists()) {
                f.mkdirs();
            }
            if (!zipFile.exists()) {
                zipFile.mkdirs();
            }
            is.close();
        } catch (Exception e) {
            print(e.toString());
        }

    }

    // 从配置文件中拿到数据源路径列表和目标路径,然后调用getAllFiles
    private void copy() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());
        try {
            InputStream is = getClass().getResourceAsStream("/config.properties");
            prop.load(is);
            String[] src = prop.getProperty("src").split(",");
            String dest = prop.getProperty("dst");
            for (String aSrc : src) {
                File file = new File(aSrc);
                getAllFiles(file, currentDate, dest, aSrc);
            }
            is.close();
        } catch (Exception e) {
            print(e.toString());
        }
    }

    private void compress() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());
        boolean flag = false;
        try {
            InputStream is = getClass().getResourceAsStream("/config.properties");
            prop.load(is);
            String zipDir = prop.getProperty("zipdir");
            String destDir = prop.getProperty("dst");
            File filein = new File(destDir);
            File[] file = filein.listFiles();
            for (int i = 0; i < file.length; i++) {
                if (file[i].isDirectory() || file[i].length() > 0) {
                    flag = true;
                    break;
                }
            }
            if (flag == true) {
                File fileout = new File(zipDir);
                if (fileout.exists() == false) {
                    fileout.mkdirs();
                }
                String strAbsFileName = fileout.getAbsolutePath()
                        + File.separator + currentDate + "_backup.zip";
                OutputStream os = new FileOutputStream(strAbsFileName);
                BufferedOutputStream bs = new BufferedOutputStream(os);
                ZipOutputStream zo = new ZipOutputStream(bs);
                zip(destDir, new File(destDir), zo, true, true);
                zo.closeEntry();
                zo.close();
                saveAll(zipDir);
            }
        } catch (Exception e) {
            print(e.toString());
        }
    }

    private void zip(String path, File basePath, ZipOutputStream zo,
                     boolean isRecursive, boolean isOutBlankDir)
            throws Exception {
        File inFile = new File(path);
        File[] files = new File[0];
        if (inFile.isDirectory()) {
            files = inFile.listFiles();
        } else if (inFile.isFile()) {
            files = new File[1];
            files[0] = inFile;
        }

        byte[] buf = new byte[1024];
        int len;
        for (int i = 0; i < files.length; i++) {
            String pathName = "";
            if (basePath != null) {
                if (basePath.isDirectory()) {
                    pathName = files[i].getPath()
                            .substring(basePath.getPath().length() + 1);
                } else {
                    pathName = files[i].getName();
                }
            } else {
                pathName = files[i].getName();
            }
            if (files[i].isDirectory()) {
                if (isOutBlankDir && basePath != null) {
                    zo.putNextEntry(new ZipEntry(pathName + "/")); // 可以使用目录也放进去
                }
                if (isRecursive) {
                    zip(files[i].getPath(), basePath, zo, isRecursive, isOutBlankDir);
                }
            } else {
                FileInputStream fin = new FileInputStream(files[i]);
                zo.putNextEntry(new ZipEntry(pathName));
                while ((len = fin.read(buf)) > 0) {
                    zo.write(buf, 0, len);
                }
                fin.close();
            }
        }
    }

    private void delete() {
        try {
            InputStream is = getClass().getResourceAsStream("/config.properties");
            prop.load(is);
            String dest = prop.getProperty("dst");
            File f = new File(dest);
            if (f.exists()) {
                File[] allFiles = f.listFiles();
                for (int i = 0; i < allFiles.length; i++) {
                    if (allFiles[i].isFile()) {
                        allFiles[i].delete();
                    } else {
                        deleteFolder(allFiles[i]);
                    }

                    print("delete: " + allFiles[i].getAbsolutePath());
                }
            }
        } catch (Exception e) {
            print(e.toString());
        }
    }

    private void deleteFolder(File folder) {
        String childs[] = folder.list();
        if (childs == null || childs.length <= 0) {
            folder.delete();
        }
        for (int i = 0; i < childs.length; i++) {
            String childName = childs[i];
            String childPath = folder.getPath() + File.separator + childName;
            File filePath = new File(childPath);
            if (filePath.exists() && filePath.isFile()) {
                filePath.delete();
            } else if (filePath.exists() && filePath.isDirectory()) {
                deleteFolder(filePath);
            }
        }
        folder.delete();
    }

    /**
     * @param dir          数据源目录
     * @param modifiedDate 当前时间
     * @param dest         目标目录
     * @param src          数据源目录的String
     *
     * @return
     *
     * @throws Exception
     */
    private String getAllFiles(File dir, String modifiedDate, String dest, String src) throws Exception {
        strBuf = new StringBuffer();
        searchDirectory(dir, modifiedDate, dest, src);
        save(dest);
        return strBuf.toString();
    }

    /**
     * @param dir          数据源目录
     * @param modifiedDate 当前时间
     * @param dest         目标目录
     * @param src          数据源目录的String
     *
     * @return
     *
     * @throws Exception
     */
    private String searchDirectory(File dir, String modifiedDate, String dest, String src) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        File[] dirs = dir.listFiles(); // 数据源目录下的所有文件,或者文件夹
        File fileout = new File(dest); // 目标文件夹
        if (!fileout.exists()) {
            fileout.mkdirs();
        }
        for (int i = 0; i < dirs.length; i++) {
            if (dirs[i].isDirectory()) {
                // 循环数据源目录下的所有文件或者文件夹,如果是文件夹则进行递归
                searchDirectory(dirs[i], modifiedDate, dest, src);
            } else {
                try {
                    // 数据源目录的最后修改时间等于当前系统执行的时间
                    if ((df.format(new Date(dirs[i].lastModified())).equals(df.format(df.parse(modifiedDate))))) {
                        File file = new File(dirs[i].toString());
                        FileInputStream fileIn = new FileInputStream(file); // 读取数据源目录中的文件内容
                        String destDir =
                                file.getAbsolutePath()
                                        .substring(src.length(), file.getAbsolutePath().lastIndexOf(File.separator));
                        File temp = new File(dest + destDir);
                        if (!temp.exists()) {
                            temp.mkdirs();
                        }
                        // fileout.getAbsolutePath == dest should be true
                        String strAbsFileName = fileout.getAbsolutePath() + destDir + File.separator + file.getName();
                        FileOutputStream fileOut = new FileOutputStream(strAbsFileName);
                        String pathName = strAbsFileName.substring(dest.length());
                        fileNameBuf.append(pathName);
                        fileNameBuf.append("\r\n");
                        byte[] br = new byte[1024];
                        while (fileIn.read(br) > 0) {
                            fileOut.write(br);
                            fileOut.flush();
                        }
                        fileIn.close();
                    }
                } catch (Exception e) {
                    print(e.toString());
                }
            }
        }
        return strBuf.toString();
    }

    private void save(String dest) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = sdf.format(new Date());
            File f = new File(dest + "//" + "filelist_" + currentDate + ".txt");
            FileWriter fout = new FileWriter(f);
            BufferedWriter buf = new BufferedWriter(fout);
            buf.write(fileNameBuf.toString());
            buf.close();
            fout.close();
        } catch (Exception e) {
            print(e.toString());
        }
    }

    private void saveAll(String zipdir) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String curentDate = sdf.format(new Date());
            File f = new File(zipdir + "//" + "filelist.txt");
            FileWriter fout = new FileWriter(f, true);
            BufferedWriter buf = new BufferedWriter(fout);
            buf.write("\r\n");
            buf.write(curentDate);
            buf.write("\r\n");
            buf.write(fileNameBuf.toString());
            buf.close();
            fout.close();
        } catch (Exception e) {
            print(e.toString());
        }
    }

}

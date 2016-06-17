package com.company.xt;

import com.company.xt.clientinfo.ThisClient;
import com.company.xt.histroyinfo.ThisClientHistroy;
import com.company.xt.serverinfo.ThisLocalConfig;
import com.company.xt.utils.FileImpl;
import com.company.xt.utils.FileUtil;
import net.sf.json.JSONArray;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Created by vive on 16/6/5.
 */
public class BackupApp {

    static FileUtil fileUtil = null;


    // 从控制台读取内容
    private static String readDataFromConsole(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(prompt);
        return scanner.nextLine();
    }


    public static void main(String[] args) {
        System.out.println("您好,启动备份程序...");

        String delayString = readDataFromConsole("多少小时后,启动程序?");
        long delay = Long.parseLong(delayString);
        try {
            System.out.println("系统将等待 " + delay + " 小时后,开始备份");
            if (delay > 0.1) {
                Thread.sleep(delay * 60 * 60 * 1000);
            }
            System.out.println("**************开始备份**************");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ThisClient client = new ThisClient();
        final String myName = client.getMyName();
        final String myServerAddress = client.getMyServerDir();

        ThisLocalConfig localConfig = new ThisLocalConfig(myName, myServerAddress);
        final long intvalTime = localConfig.getIntval();
        final long beginTime = localConfig.getBeginTime();
        JSONArray backupDirList = localConfig.getBackupDirList();
        long sleepTime = intvalTime * 60 * 60 * 1000;
        final Date date = new Date(beginTime);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd,EE, HH:mm:ss");
        System.out.println("启动备份日期是: " + dateFormat.format(date));


        ThisClientHistroy histroyServerConfig = new ThisClientHistroy(myName, myServerAddress);


        fileUtil = new FileImpl();

        while (true) {
            // 扫描本地对应的文件夹
            for (int index = 0; index < backupDirList.length(); index++) {
                // 获得本地需要备份的文件列表信息
                final String item = backupDirList.optString(index);
                // 计算文件夹下的所有文件的树状图,文件名,文件MD5,上一次修改时间
                System.out.println("对" + item + "目录下的文件进行MD5校验");

                File itemFile = new File(item);
                List<String[]> histroyConfig = histroyServerConfig.getHistroyConfig(itemFile.getName());
                List<String[]> localFilesList = fileUtil.dirInfo(item, histroyConfig, new FileUtil.Operate() {
                    @Override
                    public boolean operate(File file, String[] info) {
                        // 需要拷贝的文件
                        String path = file.getAbsolutePath();
                        fileUtil.copyFile(path, fileUtil.calcRomatePath(myName, myServerAddress, item, path), false);
                        return false;
                    }
                });

                List<String> restoreList = new ArrayList<>();
                for (String[] itemString : localFilesList) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(itemString[0]).append("<>")
                            .append(itemString[1]).append("<>")
                            .append(itemString[2]).append("<>")
                            .append(itemString[3]).append("<>")
                            .append(itemString[4]).append(System.getProperty("line.separator"));
                    restoreList.add(stringBuilder.toString());
                }
                histroyServerConfig.updateHistroyConfig(restoreList, itemFile.getName());

            }


//            histroyServerConfig.updateHistroyConfig()
            try {
                System.out.println("本轮备份工作已经完成,进入睡眠: " + dateFormat.format(new Date()));
                if (sleepTime > 1) {
                    Thread.sleep(sleepTime);
                }
                System.out.println(intvalTime + "小时的执行周期到,开始进行新一轮备份工作...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


//    private static void diffJson(JSONObject local, JSONObject romte, String serverPath, String localPath) {
//        Iterator it = local.keys();
//        while (it.hasNext()) {
//            String key = (String) it.next();
//            JSONObject localFile = local.getJSONObject(key);
//            if (localFile.has("isDirectory")) {
//                // 表示这个是目录
//
//            } else {
//                // 表示是文件
//                JSONObject remoteFile = romte.optJSONObject(key);
//                if (remoteFile == null) {
//                    //表示local新增的文件,server端没有,需要拷贝
//
//                } else {
//                    // 表示server端也存在这个文件,进行MD5比较
//                    if (localFile.getString("md5").equals(remoteFile.get("md5"))) {
//                        // 表示没有改动,忽略
//                    } else {
//                        // 表示有改动,需要拷贝
//                    }
//                }
//            }
//
//        }
//    }
}

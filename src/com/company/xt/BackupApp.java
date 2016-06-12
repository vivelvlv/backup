package com.company.xt;

import com.company.xt.clientinfo.ThisClient;
import com.company.xt.histroyinfo.ThisClientHistroy;
import com.company.xt.serverinfo.ThisLocalConfig;
import com.company.xt.utils.FileImpl;
import com.company.xt.utils.FileUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by vive on 16/6/5.
 */
public class BackupApp {
    public static void main(String[] args) {
        System.out.println("您好,启动备份程序...");
        ThisClient client = new ThisClient();
        final String myName = client.getMyName();
        final String myServerAddress = client.getMyServerDir();

        ThisLocalConfig localConfig = new ThisLocalConfig(myName, myServerAddress);
        final long intvalTime = localConfig.getIntval();
        final long beginTime = localConfig.getBeginTime();
        JSONArray backupDirList = localConfig.getBackupDirList();

        ThisClientHistroy histroyServerConfig = new ThisClientHistroy(myName, myServerAddress);
        JSONObject histroyConfigJson = histroyServerConfig.getHistroyConfigJson();
        long sleepTime = intvalTime * 60 * 60 * 1000;

        final Date date = new Date(beginTime);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd,EE, HH:mm:ss");
        System.out.println("启动备份日期是: " + dateFormat.format(date));


        FileUtil fileUtil = new FileImpl();

        while (true) {

            // 扫描本地对应的文件夹

            for (int index = 0; index < backupDirList.length(); index++) {
                // 获得本地需要备份的文件列表信息
                String item = backupDirList.optString(index);
                // 计算文件夹下的所有文件的树状图,文件名,文件MD5,上一次修改时间
                System.out.println("对" + item + "目录下的文件进行MD5校验");
                JSONObject localItemJson = fileUtil.dirInfo(item);

                // 获得该文件夹在服务器上历史记录的json
                File itemFile = new File(item);

                JSONObject romteItemJson = null;

                if (histroyConfigJson != null) {
                    romteItemJson = histroyConfigJson.optJSONObject(itemFile.getName());
                    if (romteItemJson == null) {
                        histroyConfigJson.put(itemFile.getName(), localItemJson);
                        romteItemJson = localItemJson;
//                        histroyServerConfig.updateHistroyConfig(histroyConfigJson);
                    }
                } else {
                    histroyConfigJson = new JSONObject();
                    histroyConfigJson.put(itemFile.getName(), localItemJson);
//                    histroyServerConfig.updateHistroyConfig(histroyConfigJson);
                    romteItemJson = localItemJson;
                }

                String itemServerAddr = myServerAddress + myName + File.separator;
                if (!myServerAddress.endsWith(File.separator)) {
                    itemServerAddr = myServerAddress + File.separator + myName + File.separator;
                }

                // 真正拷贝目录需要存储的server路径
                itemServerAddr += itemFile.getName() + File.separator;


                if (romteItemJson == localItemJson) {
                    //表示其中一个子目录还没被收录到服务器端的server
                    try {
                        fileUtil.copyDir(item, itemServerAddr);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    //TODO 需要比对rometItemJson与localItemJson中的内容,确定哪些是不一样的,所有不一样的地方都需要拷贝:本地新增文件|夹
                    //TODO 本地删除的,在server端暂时保留不主动移除,如果有需要可以根据histroy同步配置,清楚没有登记的,即以本地文件夹为中心遍历
                    diffJson(localItemJson, romteItemJson, itemServerAddr, item);
                }
                /**
                 * 1.这里拿到了本地的,远程的文件夹内容库的历史记录
                 * 2.以本地的为标准进行对比,如果没有找到对应的相同文件则启动备份
                 */
            }

            histroyServerConfig.updateHistroyConfig(histroyConfigJson);
            try {
                System.out.println("本轮备份工作已经完成,进入睡眠: " + dateFormat.format(new Date()));
                Thread.sleep(sleepTime);
                System.out.println(intvalTime + "小时的执行周期到,开始进行新一轮备份工作...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void diffJson(JSONObject local, JSONObject romte, String serverPath, String localPath) {
        Iterator it = local.keys();
        while (it.hasNext()) {
            String key = (String) it.next();
            JSONObject localFile = local.getJSONObject(key);
            if (localFile.has("isDirectory")) {
                // 表示这个是目录

            } else {
                // 表示是文件
                JSONObject remoteFile = romte.optJSONObject(key);
                if (remoteFile == null) {
                    //表示local新增的文件,server端没有,需要拷贝

                } else {
                    // 表示server端也存在这个文件,进行MD5比较
                    if (localFile.getString("md5").equals(remoteFile.get("md5"))) {
                        // 表示没有改动,忽略
                    } else {
                        // 表示有改动,需要拷贝
                    }
                }
            }

        }
    }
}

package com.company.xt.serverinfo;

import com.company.xt.utils.FileImpl;
import com.company.xt.utils.FileUtil;
import com.company.xt.utils.TextUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.File;
import java.util.Scanner;


public class ThisLocalConfig {
    private String detailRomteAddress;

    private FileUtil fileUtil;
    private File localConfigFile;
    private JSONObject localConfigJson;

    private JSONArray backupDirList = null;
    private long backupIntval = 0;
    private long beginTime = 0L;


    /**
     * @name 是输入的本机名称, 例如HPLC-30
     * @address 是输入的局域网备份的根地址
     * Created by vive on 16/6/2.
     */
    public ThisLocalConfig(String name, String address) {
        if (address.endsWith(File.separator)) {
            detailRomteAddress = address + name;
        } else {
            detailRomteAddress = address + File.separator + name;
        }
        fileUtil = new FileImpl();
        // 获得远程存储路径的中的localConfig文件
        localConfigFile = new File(detailRomteAddress + File.separator + "localConfig.json");

        String localConfigString = null;
        if (localConfigFile.exists() && localConfigFile.isFile()) {
            localConfigString = fileUtil.readFile(localConfigFile);
        } else {
            fileUtil.createFile(localConfigFile.getAbsolutePath());
        }

        if (!TextUtil.isEmpty(localConfigString)) {
            localConfigJson = JSONObject.fromString(localConfigString);
        }

        // 从远程的文件中读取配置信息,
        initData();
    }


    private void initData() {
        getBackupDirList();
        getBeginTime();
        getIntval();

        fileUtil.writeFile(localConfigFile, localConfigJson.toString().getBytes(), false);
    }

    public JSONArray getBackupDirList() {

        if (backupDirList != null) {
            return backupDirList;
        }


        if (localConfigJson != null) {
            backupDirList = localConfigJson.optJSONArray("dirs");
            if (backupDirList != null) {
                return backupDirList;
            }
        }

        String tempInput = readDataFromConsole("请输入需要备份的目录路径,以逗号隔开");
        backupDirList = new JSONArray();
        String[] tempInputList = tempInput.split(",");
        for (String index : tempInputList) {
            backupDirList.put(index);
        }

        if (localConfigJson == null) {
            localConfigJson = new JSONObject();
            localConfigJson.put("dirs", backupDirList);
        } else {
            localConfigJson.remove("dirs");
            localConfigJson.put("dirs", backupDirList);
        }

        return backupDirList;

    }


    public long getBeginTime() {
        if (beginTime != 0) {
            return beginTime;
        }

//        if (localConfigJson != null) {
//            beginTime = localConfigJson.optLong("begintime");
//            if (beginTime > 0) {
//                return beginTime;
//            }
//        }
        beginTime = System.currentTimeMillis();

        if (localConfigJson == null) {
            localConfigJson = new JSONObject();
            localConfigJson.put("begintime", beginTime);
        } else {
            localConfigJson.remove("begintime");
            localConfigJson.put("begintime", beginTime);
        }
        return beginTime;
    }

    public long getIntval() {
        if (backupIntval > 0) {
            return backupIntval;
        }
        if (localConfigJson != null) {
            backupIntval = localConfigJson.optLong("intval");
            if (backupIntval > 0) {
                return backupIntval;
            }
        }

        String backupIntvalString = readDataFromConsole("请输入备份周期,单位小时");

        backupIntval = Long.parseLong(backupIntvalString);

        if (localConfigJson == null) {
            localConfigJson = new JSONObject();
            localConfigJson.put("intval", backupIntval);
        } else {
            localConfigJson.remove("intval");
            localConfigJson.put("intval", backupIntval);
        }
        return backupIntval;

    }


    // 从控制台读取内容
    private String readDataFromConsole(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(prompt);
        return scanner.nextLine();
    }

}

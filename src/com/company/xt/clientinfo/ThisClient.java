package com.company.xt.clientinfo;

import com.company.xt.utils.FileImpl;
import com.company.xt.utils.TextUtil;
import net.sf.json.JSONObject;

import java.io.File;
import java.util.Scanner;

/**
 * Created by vive on 16/5/22.
 */
public class ThisClient {
    private String myName; // 我的机器名字
    private final static String NAME = "name";
    private String[] myDirs; // 我需要维护的目录列表
    private final static String DIRS = "dirs";
    private String myServerDir; // 我在server 上的位置
    private final static String ADDR = "addr";
    private String myLocalHistroy; // 我本地的所有文件的记录和状态
    private final static String LOCALHISTROY = "localhistroy";
    private String myServerHistroy; // 我在server端的文件的记录和状态
    private final static String SERVERHiSTROY = "serverhistroy";
    private long myBeginTime; // 我开始备份时间
    private final static String BEGINTIME = "begintime";
    private long myIntervalTime; // 我的备份间隔
    private final static String INTERVALTIME = "intervaltime";

    /**
     * config 这些配置文件用来,让程序知道自己的名字,自己在server上的地址,自己需要备份的目录
     */
    private JSONObject localConfig;
    private File configFile;
    private FileImpl fileUtil;
    private static final String LOCAL_CONFIG = "." + File.separator + "config.json";

    /**
     * histroy 是用来记录这些目录下的文件被备份之后的记录,包括上一次备份时间,对应的md5,对应的在server上的地址
     */


    public ThisClient() {
        fileUtil = new FileImpl();

        // 从本地去读config文件
        configFile = new File(LOCAL_CONFIG);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String configInfo = fileUtil.readFile(configFile);
        if (configInfo == null || configInfo.equals("")) {
            localConfig = null;
        } else {
            localConfig = JSONObject.fromString(configInfo);
        }

    }

    /*
    * 1.读取本地config文件,没有就创建
    * 2.从本地config文件中找到对应的serer地址,如果有那么就从server地址上读取config文件与同步历史histroy文件到本地
    * 3.本地开始扫描需要备份的文件夹,与本地同步历史进行比对,如果不一致,那么就准备执行copy操作,同时准备执行
    *
    * */

    public String getMyServerDir() {
        return getObject(ADDR, "请输入本机在server上的备份地址:");
    }

    public String getMyName() {
        return getObject(NAME, "请输入本机的名字:");
    }

    public String[] getDirs() {
        String tempDirString = getObject(DIRS, "请请输入本机需要备份的文件夹路径,以逗号分隔");
        String[] tempDirs = tempDirString.split(",");
        return tempDirs;
    }


    private String getObject(String type, String prompt) {
        if (localConfig != null) {
            String temp = localConfig.optString(type);
            if (TextUtil.isEmpty(temp)) {
                temp = readDataFromConsole(prompt);
            } else {
                return temp;
            }
            localConfig.remove(type);
            localConfig.put(type, temp);
            fileUtil.writeFile(configFile, localConfig.toString().getBytes(), false);
            return temp;
        } else {
            localConfig = new JSONObject();
            String temp = readDataFromConsole(prompt);
            localConfig.put(type, temp);
            fileUtil.writeFile(configFile, localConfig.toString().getBytes(), false);
            return temp;
        }
    }

    private String readDataFromConsole(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(prompt);
        return scanner.nextLine();
    }
}

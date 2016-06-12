package com.company.xt.clientinfo;

import com.company.xt.utils.FileImpl;
import com.company.xt.utils.FileUtil;
import com.company.xt.utils.TextUtil;
import net.sf.json.JSONObject;

import java.io.File;
import java.util.Scanner;

/**
 * Created by vive on 16/5/22.
 */
public class ThisClient {
    private String myName; // 我的机器名字
    private String myServerDir; // 我在server 上的位置

    /**
     * config 这些配置文件用来,让程序知道自己的名字,自己在server上的地址,自己需要备份的目录
     */
    private JSONObject baseConfig;
    private File configFile;
    private FileUtil fileUtil;
    private static final String BASE_CONFIG = "." + File.separator + "baseConfig.json";

    /**
     * histroy 是用来记录这些目录下的文件被备份之后的记录,包括上一次备份时间,对应的md5,对应的在server上的地址
     */


    public ThisClient() {
        fileUtil = new FileImpl();

        // 从本地去读config文件
        configFile = new File(BASE_CONFIG);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String configInfo = fileUtil.readFile(configFile);
        if (configInfo == null || configInfo.equals("")) {
            baseConfig = null;
        } else {
            baseConfig = JSONObject.fromString(configInfo);
        }

        getMyName();
        getMyServerDir();

    }

    /*
    * 1.读取本地config文件,没有就创建
    * 2.从本地config文件中找到对应的serer地址,如果有那么就从server地址上读取config文件与同步历史histroy文件到本地
    * 3.本地开始扫描需要备份的文件夹,与本地同步历史进行比对,如果不一致,那么就准备执行copy操作,同时准备执行
    *
    * */

    public String getMyServerDir() {
        if (TextUtil.isEmpty(myServerDir)) {
            myServerDir = getObject("address", "请输入本机在server上的备份地址:");
        }
        return myServerDir;
    }

    public String getMyName() {
        if (TextUtil.isEmpty(myName)) {
            myName = getObject("name", "请输入本机的名字:");
        }
        return myName;
    }

    // 如果本地baseConfig中存在的那么就从配置文件中读取,否则从控制台读取,并且把从控制台读取的内容写入到baseConfig中
    private String getObject(String type, String prompt) {
        if (baseConfig != null) {
            String temp = baseConfig.optString(type);
            if (TextUtil.isEmpty(temp)) {
                temp = readDataFromConsole(prompt);
            } else {
                return temp;
            }
            baseConfig.remove(type);
            baseConfig.put(type, temp);
            fileUtil.writeFile(configFile, baseConfig.toString().getBytes(), false);
            return temp;
        } else {
            baseConfig = new JSONObject();
            String temp = readDataFromConsole(prompt);
            baseConfig.put(type, temp);
            fileUtil.writeFile(configFile, baseConfig.toString().getBytes(), false);
            return temp;
        }
    }

    // 从控制台读取内容
    private String readDataFromConsole(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(prompt);
        return scanner.nextLine();
    }
}

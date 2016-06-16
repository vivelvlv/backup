package com.company.xt.histroyinfo;


import com.company.xt.utils.FileImpl;
import com.company.xt.utils.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vive on 16/6/5.
 */
public class ThisClientHistroy {

    private FileUtil fileUtil;
    private Map<String, File> histroyConfigFile;
    private String allpath;

    /**
     * @param name    本机的名字
     * @param address 本机在服务器上的位置
     */
    public ThisClientHistroy(String name, String address) {
        fileUtil = new FileImpl();
        allpath = address;
        if (!allpath.endsWith(File.separator)) {
            allpath += File.separator;
        }
        allpath += name + File.separator;
        histroyConfigFile = new HashMap<>();
    }

    // 获得histroy记录
    public List<String[]> getHistroyConfig(String subRomteDir) {
        File temp = new File(allpath + subRomteDir + File.separator + "histroy.list");
        if (temp.exists() && temp.isFile()) {
            histroyConfigFile.put(subRomteDir, temp);
            List<String[]> resultList = new ArrayList<>();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(temp));
                String tempString = null;
                while ((tempString = reader.readLine()) != null) {
                    // output tempString
                    String[] splitResult = tempString.split(",");
                    resultList.add(splitResult);
                }
                reader.close();
                return resultList;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {

                    }
                }
            }
        } else {
            try {
                String histroyPath = temp.getAbsolutePath();
                temp.delete();
                File fileTemp = fileUtil.createFile(histroyPath);
                histroyConfigFile.put(subRomteDir, fileTemp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    // 更新histroy记录
    public boolean updateHistroyConfig(List<String> data, String subRomteDir) {
        if (histroyConfigFile != null) {
            File tempFile = histroyConfigFile.get(subRomteDir);
            if (tempFile.exists()) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String item : data) {
                    stringBuilder.append(item);
                }
                fileUtil.writeFile(tempFile, stringBuilder.toString().getBytes(), false);
                return true;
            }
        }
        return false;
    }


}

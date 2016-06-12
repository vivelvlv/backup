package com.company.xt.histroyinfo;


import com.company.xt.utils.FileImpl;
import com.company.xt.utils.FileUtil;
import com.company.xt.utils.TextUtil;
import net.sf.json.JSONObject;

import java.io.File;

/**
 * Created by vive on 16/6/5.
 */
public class ThisClientHistroy {

    private String histroyConfigString;
    private JSONObject histroyConfigJson;
    private FileUtil fileUtil;
    private File histroyConfigFile;

    /**
     * @param name    本机的名字
     * @param address 本机在服务器上的位置
     */
    public ThisClientHistroy(String name, String address) {
        fileUtil = new FileImpl();
        String romtePath = address;
        if (!romtePath.endsWith(File.separator)) {
            romtePath += File.separator;
        }
        romtePath += name + File.separator + "histroy.json";

        histroyConfigFile = new File(romtePath);
        if (histroyConfigFile.exists() && histroyConfigFile.isFile()) {
            histroyConfigString = fileUtil.readFile(histroyConfigFile);
        } else {
            fileUtil.createFile(romtePath);
            histroyConfigFile = new File(romtePath);
        }

        if (!TextUtil.isEmpty(histroyConfigString)) {
            histroyConfigJson = JSONObject.fromString(histroyConfigString);
        }

    }

    // 获得histroy记录
    public JSONObject getHistroyConfigJson() {
        if (histroyConfigJson != null) {
            return histroyConfigJson;
        }
        return null;
    }

    // 更新histroy记录
    public boolean updateHistroyConfig(JSONObject data) {
        if (histroyConfigFile != null) {
            fileUtil.writeFile(histroyConfigFile, data.toString().getBytes(), false);
            histroyConfigJson = data;
            return true;
        }
        return false;
    }


}

package com.company.test;

import net.jimmc.jshortcut.JShellLink;

/**
 * Created by vive on 16/6/15.
 */
public class CreateShortcust {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String fileFolderPath = "/Users/vive/Desktop/Mou.app";
        String writeFolderPath = "/Users/vive/Desktop/dex-test/eee/";
        createShortCut(fileFolderPath, writeFolderPath, null);
//		String path=getShortCutRealPath(writeFolderPath);
//		System.out.println(path);
//		String targetPath="D:\\Test1";
//		createShortCut(targetPath+"\\Tomcat\\bin\\startup.bat", targetPath+"\\ShortCut\\startup.bat", null);
    }

    /**
     * ´´½¨¿ì½Ý·½Ê½
     *
     * @param fileorFolderPath  Ô´ÎÄ¼þ¼ÐÂ·¾¶
     * @param writeShortCutPath Ä¿±êÎÄ¼þÂ·¾¶£¨¿ì½Ý·½Ê½ÐÍ£©
     */
    public static void createShortCut(String fileorFolderPath, String writeShortCutPath, String arguments) {
        JShellLink link = new JShellLink();
        writeShortCutPath = writeShortCutPath.replaceAll("/", "\\");
        String folder = writeShortCutPath.substring(0, writeShortCutPath.lastIndexOf("\\") + 1);
        String name = writeShortCutPath.substring(writeShortCutPath.lastIndexOf("\\") + 1,
                writeShortCutPath.length());
        link.setName(name);//Ä¿±ê¿ì½Ý·½Ê½ÎÄ¼þ¼ÐÃû³Æ
        link.setFolder(folder);//Ä¿µÄ¿ì½Ý·½Ê½ÎÄ¼þÂ·¾¶Æ¬¶Î
        link.setPath(fileorFolderPath);
        if (arguments != null && !"".equals(arguments.trim())) {
            link.setArguments(arguments);
        }
        link.save();
    }

    public static String getShortCutRealPath(String fileFolderPath) {
        fileFolderPath = fileFolderPath.replaceAll("/", "\\");
        String folder = fileFolderPath.substring(0, fileFolderPath.lastIndexOf("\\"));
        String name = fileFolderPath.substring(fileFolderPath.lastIndexOf("\\"),
                fileFolderPath.length());
        JShellLink link = new JShellLink(folder, name);
        link.load();
        return link.getPath();
    }

}

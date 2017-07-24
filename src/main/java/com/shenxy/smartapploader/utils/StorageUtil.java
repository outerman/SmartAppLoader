package com.shenxy.smartapploader.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

public class StorageUtil {
    public enum Type {
        log,

        image,

        savedMedia,

        audio,

        video,

        html,

        app,

        download,

        basePath,

        res,

        apkPlugin,

        tmp;


        public static Type fromString(String name) {
            try {
                return Type.valueOf(name);
            } catch (Exception e) {
                return log;
            }
        }

    }

    /**
     * 获取存储文件的路径，不含文件名，以“/”结尾
     * 优先顺序：存储卡 -> 程序安装目录
     *
     * @return
     */
    static String basePath;
    public static String GetStorePath(Type type) {
//        String basePath;
        if (StringUtils.isBlank(basePath)) {
            if (SdCardUtils.isSdcardWritable()) {
                basePath = Environment.getExternalStorageDirectory() + "/rrtimes/" + "yijia/";
            } else {
                //没有sd卡的时候直接使用文件名即可
                basePath = "Device" + "/rrtimes/" + "yijia/";
//			return "";
            }
        }

        String dirPath = "";
        if (type.equals(Type.log)) {
            dirPath = basePath + "log/";
        } else if (type.equals(Type.image)) {
            dirPath = basePath + "img/";
        } else if (type.equals(Type.savedMedia)) {
            dirPath = basePath + "savedMedia/";
        } else if (type.equals(Type.audio)) {
            dirPath = basePath + "audio/";
        } else if (type.equals(Type.video)) {
            dirPath = basePath + "video/";
        } else if (type.equals(Type.html)) {
            dirPath = basePath + "html/";
        } else if (type.equals(Type.res)) {
            dirPath = basePath + "res/";
        } else if (type.equals(Type.app)) {
            dirPath = basePath + "app/";
        } else if (type.equals(Type.download)) {
            dirPath = basePath + "download/";
        } else if (type.equals(Type.apkPlugin)) {
            dirPath = basePath + "apkplugin/";
        } else if (type.equals(Type.basePath)) {
            dirPath = basePath ;
        } else if (type.equals(Type.tmp)) {
            dirPath = basePath + "tmp/"; ;
        }

        return checkDirPath(dirPath, type);
    }

    //验证路径是否可用
    private static String checkDirPath(String dirPath, Type type) {
        if (StringUtils.isNotBlank(dirPath)) {
            File dir = new File(dirPath);
            String nomediaFileName = dirPath + ".nomedia";//创建该文件,使得系统图库,不会把缓存的图片显示出来
            if (!dir.exists()) {
                if (dir.mkdirs()) {
                    if (type != Type.savedMedia && type != Type.basePath) {   //用于存储"另存为"的图片等,应该可以让图库看到
                        try {
                            new File(nomediaFileName).createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return dirPath;
                } else {
                    //如果创建文件夹失败，表示对该卡没有写权限，尝试各个卡sdcard0,sdcard1,sdcard2
                    if (dirPath.contains("sdcard0")) {
                        return checkDirPath(dirPath.replace("sdcard0", "sdcard1"), type);
                    }
                    else if (dirPath.contains("sdcard1")){
                        return checkDirPath(dirPath.replace("sdcard1", "sdcard2"), type);
                    }
                    else {
                        return "";
                    }
                }
            }
            else {
                if (type != Type.savedMedia && type != Type.basePath) {
                    //老版本用户,文件夹已经存在,但是nomedia文件未创建
                    File nomediaFile = new File(nomediaFileName);
                    if (!nomediaFile.exists()) {
                        try {
                            nomediaFile.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                //老版本用户,如果已经在 basePath下创建了,则删除
                File nomediaFile = new File(basePath + ".nomedia");
                if (nomediaFile.exists()) {
                    nomediaFile.delete();
                }

            }
            return dirPath;
        } else {
            return "";
        }
    }

    /**
     * 获取单个文件的MD5值！
     *
     * @param file
     * @return
     */
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest;
        FileInputStream in;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }
}

package com.shenxy.smartapploader.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by shenxy on 15/3/27.
 */
public class AssetCopyUtil {
    private static String TAG = "AssetCopyUtil";
    /**
     * 从assets目录下拷贝整个文件夹，不管是文件夹还是文件都能拷贝
     *
     * @param context
     *            上下文
     * @param rootDirFullPath
     *            文件目录，要拷贝的目录如assets目录下有一个SBClock文件夹：SBClock
     * @param targetDirFullPath
     *            目标文件夹位置如：/sdcrad/SBClock
     */
    public static void copyFolderFromAssets(Context context, String rootDirFullPath, String targetDirFullPath) {
        Log.d(TAG, "copyFolderFromAssets " + "rootDirFullPath-" + rootDirFullPath + " targetDirFullPath-" + targetDirFullPath);
        try {
            File targetDir = new File(targetDirFullPath);
            if (!targetDir.exists()){
                targetDir.mkdirs();
            }
            String[] listFiles = context.getAssets().list(rootDirFullPath);// 遍历该目录下的文件和文件夹
            if (listFiles.length > 0) { //是文件夹
                for (String strFilename : listFiles) {// 看起子目录是文件还是文件夹，这里只好用.做区分了
                    Log.d(TAG, "name-" + rootDirFullPath + "/" + strFilename);
                    if (isFileByName(context, strFilename, rootDirFullPath)) {// 文件
                        copyFileFromAssets(context, rootDirFullPath + "/" + strFilename, targetDirFullPath + "/" + strFilename);
                    } else {// 文件夹
                        String childRootDirFullPath = rootDirFullPath + "/" + strFilename;
                        String childTargetDirFullPath = targetDirFullPath + "/" + strFilename;
                        new File(childTargetDirFullPath).mkdirs();
                        copyFolderFromAssets(context, childRootDirFullPath, childTargetDirFullPath);
                    }
                }
            }
            else { //文件
                //...该方法只拷贝目录
            }
        } catch (IOException e) {
            Log.d(TAG, "copyFolderFromAssets " + "IOException-" + e.getMessage());
            Log.d(TAG, "copyFolderFromAssets " + "IOException-" + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    //返回是否文件
    private static boolean isFileByName(Context context, String filename, String dirPath) {
        try {
            String[] listFiles = context.getAssets().list(dirPath + "/" + filename);
            if (listFiles.length > 0){//打包时候，空文件夹不会打包到assets里，通过有没有下级文件来判断
                return false;
            }
            else {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 从assets目录下拷贝文件
     *
     * @param context
     *            上下文
     * @param assetsFilePath
     *            文件的路径名如：SBClock/0001cuteowl/cuteowl_dot.png
     * @param targetFileFullPath
     *            目标文件路径如：/sdcard/SBClock/0001cuteowl/cuteowl_dot.png
     */
    public static void copyFileFromAssets(Context context, String assetsFilePath, String targetFileFullPath) {
        Log.d(TAG, "copyFileFromAssets ");
        InputStream assestsFileImputStream;
        try {
            assestsFileImputStream = context.getAssets().open(assetsFilePath);
            FileUtils.copyFile(assestsFileImputStream, targetFileFullPath);
        } catch (IOException e) {
            Log.d(TAG, "copyFileFromAssets " + "IOException-" + e.getMessage());
            e.printStackTrace();
        }
    }

}

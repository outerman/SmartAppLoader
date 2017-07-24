package com.shenxy.smartapploader.app;

import android.content.Context;
import android.os.AsyncTask;

import com.shenxy.smartapploader.framework.installer.ZipInstaller;
import com.shenxy.smartapploader.utils.AssetCopyUtil;
import com.shenxy.smartapploader.utils.download.DownloadHelper;

import java.io.File;

//import com.chanjet.workcircle.app.BaseActivity;
//import com.chanjet.workcircle.phonegap.framework.installer.ZipInstaller;
//import com.chanjet.workcircle.util.DownloadHelper;

/**
 * Created by shenxy on 15/5/5.
 */
public class PreInstallAppAndOpenTask extends AsyncTask<Integer, Void, Void> {
    private PreInstallListener preInstallListener;
    private Context context;
    public void setPreInstallListener(PreInstallListener preInstallListener){
        this.preInstallListener = preInstallListener;
    }

    public PreInstallAppAndOpenTask(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        if (context instanceof BaseActivity && !((Activity) context).isFinishing()){
//            ((BaseActivity) context).showDialog(BaseActivity.DIALOG_PROGRESS_LOADING);
//        }
    }

    @Override
    protected Void doInBackground(Integer... strings) {
        Integer appId = strings[0];
        Integer version = strings[1];

        if (appId == null || version == null) {
            return null;
        }

        preInstallZip(appId, version);
        return null;
    }

    public void preInstallZip(Integer appId, Integer version) {
        try {
            String installRootPath = ZipInstaller.getInstallRoot();
            File zipFile = new File(installRootPath + appId + ".zip");
            //0、删除老zip（如果有的话）
            if (zipFile.exists()){
                zipFile.delete();
            }
            //1、拷贝压缩包到file文件夹
            AssetCopyUtil.copyFileFromAssets(context, appId + ".zip", installRootPath + appId + ".zip");
            //2、删除老文件（如果有的话）
            String appInstallFolder = installRootPath + appId + "/";
            AppZipDownloading.delFolder(appInstallFolder);
            //2.5、创建文件夹
            new File(appInstallFolder).mkdir();
            //3、解压缩到目标文件夹
            zipFile = new File(installRootPath + appId + ".zip");
            DownloadHelper.upZipFile(zipFile, appInstallFolder);
            //3.5、生成版本文件，用于下次升级检查
            AppZipDownloading.makeVersionFile(appInstallFolder, version);

            //4、删除file文件夹中的压缩包
            if (zipFile.exists()){
                zipFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
//        if (context instanceof BaseActivity && !((Activity) context).isFinishing()){
//            ((BaseActivity) context).removeDialog(BaseActivity.DIALOG_PROGRESS_LOADING);
//        }
        if (preInstallListener != null){
            preInstallListener.OnPreInstallFinish();
        }
    }

    public interface PreInstallListener{
        public void OnPreInstallFinish();
    }

}
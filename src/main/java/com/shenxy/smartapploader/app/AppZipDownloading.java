package com.shenxy.smartapploader.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.shenxy.smartapploader.AppConstants;
import com.shenxy.smartapploader.R;
import com.shenxy.smartapploader.auth.AppPluginAuthManager;
import com.shenxy.smartapploader.framework.installer.AppVersion;
import com.shenxy.smartapploader.framework.installer.ZipInstaller;
import com.shenxy.smartapploader.framework.startInfo.AppStartInfo;
import com.shenxy.smartapploader.utils.CSPFileViewer;
import com.shenxy.smartapploader.utils.download.DownloadHelper;
import com.shenxy.smartapploader.utils.JSONExtension;
import com.shenxy.smartapploader.utils.OnDownloadStatusChangedListener;
import com.shenxy.smartapploader.utils.SdCardUtils;
import com.shenxy.smartapploader.utils.StatusEvent;
import com.shenxy.smartapploader.utils.StorageUtil;
import com.shenxy.smartapploader.utils.StringUtils;
import com.shenxy.smartapploader.wiget.FasterAnimationsContainer;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

//import de.greenrobot.event.EventBus;

//
//import com.chanjet.core.utils.JSONExtension;
//import com.chanjet.im.event.StatusEvent;
//import com.chanjet.libutil.util.SdCardUtils;
//import com.chanjet.libutil.util.StorageUtil;
//import com.chanjet.libutil.util.StringUtils;
//import com.chanjet.workcircle.AppConstants;
//import com.chanjet.workcircle.R;
//import com.chanjet.workcircle.app.BaseActivity;
//import com.chanjet.workcircle.phonegap.auth.AppPluginAuthManager;
//import com.chanjet.workcircle.phonegap.framework.installer.AppVersion;
//import com.chanjet.workcircle.phonegap.framework.installer.ZipInstaller;
//import com.chanjet.workcircle.phonegap.framework.startInfo.AppStartInfo;
//import com.chanjet.workcircle.util.CSPFileViewer;
//import com.chanjet.workcircle.util.DownloadHelper;
//import com.chanjet.workcircle.util.OnDownloadStatusChangedListener;
//import com.chanjet.workcircle.widget.splash.FasterAnimationsContainer;


/**
 * Created by shenxy 2014-10-17
 * 下载app的界面
 */
public class AppZipDownloading extends Activity {//BaseActivity {
    public static final String EVENT_ZIP_APP_INSTALL_SUCCEED = "EVENT_ZIP_APP_INSTALL_SUCCEED";
    public static final String EVENT_ZIP_APP_INSTALL_FAILED = "EVENT_ZIP_APP_INSTALL_FAILED";

    private String currentDownloadSizeText;
//    Timer timer = new Timer();
    boolean isTotalSizeInitialed = false;
    long totalSize = 0l;
    String appFolderName;
    String appZipFilePath;
    int appId;
    AppStartInfo startInfo;

    TextView tv_total;
    TextView tv_current;
    TextView tv_status;

    Handler handler = new Handler();
    boolean isDownloading = false;
    private Runnable refreshDownloadingSizeRunnable = new Runnable() {
        @Override
        public void run() {
            if (isDownloading) {
                if (currentDownloadSizeText == null || currentDownloadSizeText.length() < 1) {
                    currentDownloadSizeText = "0B";
                }

                tv_current.setText(currentDownloadSizeText);

                handler.postDelayed(refreshDownloadingSizeRunnable, 1000);
            }
        }
    };

    ImageView _circleListRefreshingAnimation;// = (ImageView) findViewById(R.id.circle_list_refreshing_animation);
    private static final int[] IMAGE_RESOURCES_LOADIND = AppConstants.IMAGE_RESOURCES_LOADIND;
    FasterAnimationsContainer fasterAnimationsContainer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.csp_file_downloading);

        startInfo = (AppStartInfo) getIntent().getSerializableExtra(AppIntentAction.APP_START_INFO);
        String url = "";
        if (startInfo.appInfo != null && startInfo.appInfo.downUrl != null) {
            url = startInfo.appInfo.downUrl.APH; //getIntent().getStringExtra(AppIntentAction.APP_URL);
        }
        appFolderName = ZipInstaller.getAppFolderName(startInfo.appId);// getIntent().getStringExtra(AppIntentAction.APP_INSTALL_FOLDERNAME);

        if (StringUtils.isBlank(url) || StringUtils.isBlank(appFolderName) || startInfo == null){
            showErrorMsg("应用安装包信息不完整，无法下载安装！");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
            return;
        }
        appId = startInfo.appId;

//        setView(R.layout.csp_file_downloading);
        tv_total = (TextView) findViewById(R.id.totalSize);
        tv_current = (TextView) findViewById(R.id.size);
        tv_status = (TextView) findViewById(R.id.status);
//        tv_status.setText("下载中...");

//        setTitleBar(getResources().getString(R.string.title_back), "应用下载", null);

        tv_total.setText("0B");
        tv_current.setText("0B");

        _circleListRefreshingAnimation = (ImageView) findViewById(R.id.imate_chat_list_refreshing_animation);
        if (_circleListRefreshingAnimation != null) {
            fasterAnimationsContainer = new FasterAnimationsContainer(_circleListRefreshingAnimation);
            fasterAnimationsContainer.addAllFrames(IMAGE_RESOURCES_LOADIND, AppConstants.IMAGE_LOADIND_DURATION);
//            fasterAnimationsContainer.start();
        }
        startAnimation();

        startDownload(url);
    }

    private void startDownload(final String url) {
        String filePath = StorageUtil.GetStorePath(StorageUtil.Type.download);
        if (!SdCardUtils.isSdcardWritable() || StringUtils.isBlank(filePath)){
            showErrorMsg("未安装SD卡或权限不足，不能下载应用！");
        }
        else if (!SdCardUtils.isSdcardEnoughStorage(filePath)){
            showErrorMsg("SD卡空间不足，不能下载应用！");
        }
        else {
            try {
                tv_status.setText("获取应用权限配置...");
                //获取服务端对于该appid的插件权限配置
                AppPluginAuthManager.getInstance().updateAppPluginAuthConfig(appId, new AppPluginAuthManager.AppPluginAuthUpdateListener() {
                    @Override
                    public void OnUpdateSucceed() {
                        tv_status.setText("准备下载...");
                        doDownload(url);
                    }

                    @Override
                    public void OnUpdateFailed() {
                        showErrorMsg("获取应用权限失败！");
                    }
                });
            }
            catch (Exception e){
                showErrorMsg("获取应用权限失败！");
            }
        }
    }

    private void doDownload(String url) {
        DownloadHelper.start(url, new
                        OnDownloadStatusChangedListener() {
                            @Override
                            public void OnProgressChanged(final long total, final long current) {
                                totalSize = total;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!isTotalSizeInitialed) {
                                            tv_status.setText("下载中...");
                                            tv_total.setText(CSPFileViewer.getFileSizeForDisplay(total));
                                            isTotalSizeInitialed = true;
                                        }
                                        currentDownloadSizeText = CSPFileViewer.getFileSizeForDisplay(current);
//                            tv_current.setText(current + "");
                                    }

                                });

                            }

                            @Override
                            public void OnError(Exception ex) {
                                showErrorMsg(getResources().getString(R.string.error_network));
                                EventBus.getDefault().post(new StatusEvent(EVENT_ZIP_APP_INSTALL_FAILED, startInfo.appId, startInfo));
                            }

                            @Override
                            public void OnDownloadComplete(final String fullPath) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        appZipFilePath = fullPath;
                                        tv_total.setText(CSPFileViewer.getFileSizeForDisplay(totalSize));
                                        currentDownloadSizeText = CSPFileViewer.getFileSizeForDisplay(totalSize);//tv_current.setText(totalSize + "");

                                        if (DownloadHelper.getFileExtNameFromUri(fullPath).trim().equals("zip")) {
                                            tv_status.setText("下载完成，开始安装...");
                                        }
                                        else {
                                            tv_status.setText("下载完成，安装包格式错误...");
                                            EventBus.getDefault().post(new StatusEvent(EVENT_ZIP_APP_INSTALL_FAILED, startInfo.appId, startInfo));
                                        }
                                    }
                                });
                            }

                            @Override
                            public void OnZipFileUnCompressed(final String path) {
                                try {
//                        InputStream iStream = getAssets().open(appFolderName + "/index.html");
                                    String appInstallFolder = ZipInstaller.getInstallRoot() + appFolderName;

                                    //删除原有老文件
                                    delAllFile(appInstallFolder);
                                    //拷贝新文件
                                    copyFolder(path, appInstallFolder);
                                    //生成版本文件，用于下次升级检查
                                    makeVersionFile(appInstallFolder, startInfo.appInfo.version);
                                    //删除源文件
                                    delAllFile(path);
                                    //删除压缩包
                                    File zipFile = new File(appZipFilePath);
                                    zipFile.deleteOnExit();

                                    if (!AppZipDownloading.this.isFinishing()) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                tv_total.setText(CSPFileViewer.getFileSizeForDisplay(totalSize));
                                                currentDownloadSizeText = CSPFileViewer.getFileSizeForDisplay(totalSize);//tv_current.setText(totalSize + "");
                                                tv_status.setText("安装完成，打开应用...");
                                                //通知安装完成
//                                                EventBus.getDefault().post(new StatusEvent(AppManager.EVENT_INSTALL_SUCCEED, startInfo.appId, startInfo));

                                                EventBus.getDefault().post(new StatusEvent(EVENT_ZIP_APP_INSTALL_SUCCEED, startInfo.appId, startInfo));
                                            }
                                        });
                                    } else {
                                        Log.d("AppZipDownloading", "cancel loading lightapp, Activity is finishing");
                                    }

                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    }, 1000);

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    showErrorMsg("安装失败！");
                                    EventBus.getDefault().post(new StatusEvent(EVENT_ZIP_APP_INSTALL_FAILED, startInfo.appId, startInfo));
                                }
                            }
                        }
        );
    }

    //生成版本文件，用于下次升级检查
    public static void makeVersionFile(String appInstallFolder, Integer version) {
        File startupFile = new File(appInstallFolder + "/version.json");
        if (startupFile.exists()){
            startupFile.deleteOnExit();
        }

        AppVersion appVersion = new AppVersion();
        appVersion.version = version;
        FileOutputStream outputStream = null;
        try {
            if (startupFile.createNewFile()){
                outputStream = new FileOutputStream(startupFile);
                outputStream.write(JSONExtension.toJSONString(appVersion).getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    private String uri2Path(Uri uri){
//        String[] proj = { MediaStore.Images.Media.DATA };
//        Cursor actualimagecursor = managedQuery(uri,proj,null,null,null);
//        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        actualimagecursor.moveToFirst();
//        String img_path = actualimagecursor.getString(actual_image_column_index);
//        return img_path;
//    }

    private void showErrorMsg(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                playErrorMsgAnimation(msg);

                stopAnimation();

                TextView tv = (TextView) findViewById(R.id.status);
                if (tv != null) {
                    tv.setText(msg);
                }
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finishActivity(RESULT_CANCELED);
            }
        }, 3000);
    }

    /**
     * 复制整个文件夹内容
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public void copyFolder(String oldPath, String newPath) {

        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a=new File(oldPath);
            String[] file=a.list();
            File temp=null;
            for (int i = 0; i < file.length; i++) {
                if(oldPath.endsWith(File.separator)){
                    temp=new File(oldPath+file[i]);
                }
                else{
                    temp=new File(oldPath+File.separator+file[i]);
                }

                if(temp.isFile()){
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ( (len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if(temp.isDirectory()){//如果是子文件夹
                    copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);
                }
            }
        }
        catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace();
        }
    }

    //删除指定文件夹下所有文件
    //param path 文件夹完整绝对路径
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    //删除文件夹
    //param folderPath 文件夹完整绝对路径
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isDownloading = false;
//        timer.cancel();
    }

    private void startAnimation() {
//        AnimationDrawable draw = (AnimationDrawable) ((ImageView) this.findViewById(R.id.imate_chat_list_refreshing_animation)).getDrawable();
//        draw.start();
        if (fasterAnimationsContainer != null) {
            fasterAnimationsContainer.start();
        }

//        timer.schedule(new TimerTask() {
//            public void run() {
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
//                Log.d(TAG, "execute task!" + this.scheduledExecutionTime());
//            }
//        }, 500, 1000);
        isDownloading = true;
        handler.postDelayed(refreshDownloadingSizeRunnable, 500);
    }

    private void stopAnimation() {
//        ImageView animationImageView = (ImageView) this.findViewById(R.id.imate_chat_list_refreshing_animation);
//        if (animationImageView != null) {
//
//            AnimationDrawable draw = (AnimationDrawable) animationImageView.getDrawable();
//            if (draw != null) {
//                draw.stop();
//            }
//        }
        if (fasterAnimationsContainer != null) {
            fasterAnimationsContainer.stop();
        }
        isDownloading = false;
//        timer.cancel();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            CSPFileViewer.getInstance().cancelDownload();

            isDownloading = false;
            stopAnimation();
            finish();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

//    @Override
//    public void onClick(View v) {
//        super.onClick(v);
//
//        switch (v.getId()) {
//            case R.id.topbar_ll_left:
////                timer.cancel();
//                isDownloading = false;
//                stopAnimation();
//                finish();
//                break;
//            default:
//                break;
//        }
//    }

}

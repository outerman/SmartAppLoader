//package com.shenxy.smartapploader.framework.installer;
//
//import android.app.Activity;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.res.Resources;
//import android.os.RemoteException;
//import android.view.View;
//import android.widget.Toast;
//
//import com.shenxy.smartapploader.framework.loader.AppLoaderEnv;
//import com.shenxy.smartapploader.request.MyWorkQuery;
//import com.shenxy.smartapploader.utils.ChanjetDialog;
//import com.shenxy.smartapploader.utils.StorageUtil;
//import com.shenxy.smartapploader.workTmp.OrgManager;
//
//import PluginManager;
//
//import java.io.File;
//import java.io.InputStream;
//import java.util.List;
//
////import com.chanjet.libutil.util.FileUtils;
////import com.chanjet.libutil.util.StorageUtil;
////import com.chanjet.rxworkcircle.view.activity.BaseActivity;
////import com.chanjet.workcircle.R;
////import com.chanjet.workcircle.application.BaseApplication;
////import com.chanjet.workcircle.model.ChangeOrgModel;
////import com.chanjet.workcircle.phonegap.app.NativePluginUpdateActivity;
////import com.chanjet.workcircle.phonegap.framework.loader.AppLoaderEnv;
////import com.chanjet.workcircle.phonegap.receiver.NativePluginUpdateReceiver;
////import com.chanjet.workcircle.request.MyWorkQuery;
////import com.chanjet.workcircle.util.DownloadApkAndInstallUtil;
////import com.chanjet.workcircle.util.OrgManager;
////import com.chanjet.workcircle.widget.ChanjetDialog;
////import com.morgoo.droidplugin.pm.PluginManager;
////import com.morgoo.helper.compat.PackageManagerCompat;
//
///**
// * Created by shenxy on 16/4/18.
// * Native插件的安装类
// */
//public class NativePluginInstaller extends BaseInstaller {
//    private final int INSTALL_SUCCESS_RET_CODE = 1;
//    private final String DEFAULT_PACKAGE_PATH = StorageUtil.GetStorePath(StorageUtil.Type.apkPlugin);
//    public static final String DEFAULT_DOWNLOAD_PACKAGE_PATH = StorageUtil.GetStorePath(StorageUtil.Type.apkPlugin) + "download/";
//    private ChanjetDialog downloadDialog;
//
//    public NativePluginInstaller(AppLoaderEnv appLoaderEnv) {
//        super(appLoaderEnv);
//
//        assert (getActivity() != null && getStartInfo() != null && getStartInfo().appInfo != null
//                && getStartInfo().appInfo.version != null && getStartInfo().appInfo.extension != null);
//
//    }
//
//    @Override
//    public void preinstall() {
//        //是否已经安装了最新版本
//        if (isInstalled()) {
//            return;
//        }
//
//        String pluginApkName = getApkLocalFileName(getStartInfo().appId);
//        String preInstallFilePath = DEFAULT_PACKAGE_PATH + pluginApkName;
//        String downloadFilePath = DEFAULT_DOWNLOAD_PACKAGE_PATH + pluginApkName;
//        //上次下载的包 和 内置的包,如果有比服务端最新的,则安装其中最新的
//        int downloadPackageVersion = -1;
//        int assetsPackageVersion = -1;
//        int installedVersion = -1;
//        if (new File(downloadFilePath).exists()) { //下载完成后才拷贝出来的,所以只要存在就是下载完成的
//            downloadPackageVersion = getApkPackageVersionCode(getActivity(), downloadFilePath);
//        }
//
//        assetsPackageVersion = getAssetsPackageVersion(getActivity(), pluginApkName, preInstallFilePath);
//        installedVersion = getInstalledVersionCode(getStartInfo().appInfo.extension.androidPackage);
//
//        if (downloadPackageVersion > 0
//                && downloadPackageVersion >= getStartInfo().appInfo.version
//                && downloadPackageVersion > installedVersion
//                && downloadPackageVersion > assetsPackageVersion) {
//            installApp(downloadFilePath);
//        } else if (assetsPackageVersion > 0
//                && assetsPackageVersion >= getStartInfo().appInfo.version
//                && assetsPackageVersion > installedVersion) {
//
//            if (!new File(preInstallFilePath).exists()) {
//                copyPluginToDstFolder(getActivity(), pluginApkName, preInstallFilePath);
//            }
//            installApp(preInstallFilePath);
//        }
//
//        //本地没有最新的包,不用管了
//    }
//
//    @Override
//    public boolean installApp(String targetFilePath) {
//        try {
//            File installFile = new File(targetFilePath);
//            if (!installFile.exists()) {
//                return false;
//            }
//
//            //检查orgId,只为当前企业安装
//            if (getStartInfo().entId == null || !getStartInfo().entId.equals(OrgManager.getInstance().getCurOrgId())) {
//                return false;
//            }
//
//            //检查待安装apk包的包名,和本地缓存的比较,因为传入的startInfo中的packageName可能是NativePluginUpdateActivity传入的,与升级包配套的错误值
//            PackageManager pm = getActivity().getPackageManager();
//            PackageInfo info = pm.getPackageArchiveInfo(targetFilePath, PackageManager.GET_ACTIVITIES);
//            if (info == null) {
//                return false;
//            }
//
//            MyWorkQuery.MyWorkItem cachedAppInfo = new ChangeOrgModel().getCacheWorkItemById(getStartInfo().entId, String.valueOf(getStartInfo().appId));
//            if (cachedAppInfo == null || cachedAppInfo.extension == null
//                    || (!info.packageName.equals(cachedAppInfo.extension.androidPackage) && !info.packageName.equals(cachedAppInfo.extension.runtimePackage))) {
//                return false;
//            }
//
//            showLoading();
//
//            int installRet = PluginManager.getInstance().installPackage(
//                    targetFilePath,
//                    PackageManagerCompat.INSTALL_REPLACE_EXISTING);
//
//            if (installRet == INSTALL_SUCCESS_RET_CODE) { //安装成功后,把安装文件重命名一下;省掉每次preinstall都能检查到
//                File target = new File(targetFilePath);
//                target.renameTo(new File(target.getPath() + "_install.apk"));
//            }
//
//            hideLoading();
//
//            showInstallResult(installRet);
//
//            return INSTALL_SUCCESS_RET_CODE == installRet;
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    private void hideLoading() {
//        if (getActivity() instanceof BaseActivity) {
//            ((BaseActivity)getActivity()).dismissDialog(BaseActivity.DIALOG_PROGRESS_LOADING);
//        }
//        else if (getActivity() instanceof com.chanjet.workcircle.app.BaseActivity) {
//            ((com.chanjet.workcircle.app.BaseActivity)getActivity()).dismissDialog(BaseActivity.DIALOG_PROGRESS_LOADING);
//        }
//    }
//
//    private void showLoading() {
//        if (getActivity() instanceof BaseActivity) {
//            ((BaseActivity)getActivity()).showDialog(BaseActivity.DIALOG_PROGRESS_LOADING);
//        }
//        else if (getActivity() instanceof com.chanjet.workcircle.app.BaseActivity) {
//            ((com.chanjet.workcircle.app.BaseActivity)getActivity()).showDialog(BaseActivity.DIALOG_PROGRESS_LOADING);
//        }
//    }
//
//    //检查已安装的插件的版本,是否已经是最新的
//    @Override
//    public boolean isInstalled() {
//        return getInstalledVersionCode(getStartInfo().appInfo.extension.androidPackage) >= getStartInfo().appInfo.version;//判空在loader里已经做过了
//    }
//
//    protected int getInstalledVersionCode(String packageName) {
//        try {
//            List<PackageInfo> infos = PluginManager.getInstance().getInstalledPackages(0);
//
//            if (infos != null && infos.size() > 0) {
//                for (PackageInfo info : infos) {
//                    //如果已经安装最新版本,则不用检查预安装了
//                    if (info.packageName.equals(getStartInfo().appInfo.extension.androidPackage)) { //判空在loader里已经做过了
//                        return info.versionCode;
//                    }
//                }
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//
//        return -1;
//    }
//
//    @Override
//    public void downloadApp() {
//        String downloadUrl = getStartInfo().appInfo.downUrl.APH;
//        String packageLocalPath = getApkLocalFileName(getStartInfo().appId);
//
//        Intent contentIntent = new Intent();
//        contentIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        contentIntent.putExtra(NativePluginUpdateActivity.installFilePathExtraName, packageLocalPath);
//        contentIntent.putExtra(NativePluginUpdateReceiver.appIdExtraName, getStartInfo().appId);
//        contentIntent.putExtra(NativePluginUpdateReceiver.orgIdExtraName, getStartInfo().entId);
//        contentIntent.putExtra(NativePluginUpdateActivity.actionExtraName, NativePluginUpdateActivity.ActionType.install.name());
//        contentIntent.setAction(NativePluginUpdateActivity.intentFilter);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                downloadDialog = new ChanjetDialog(getActivity());
//                downloadDialog.setTitleVisible(false);
//                downloadDialog.setDialogText("需要下载最新版本的应用安装包,会花费一些流量,是否继续?");
//                downloadDialog.setButton("确定", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                new DownloadApkAndInstallUtil(BaseApplication.getContext())
//                                        .downloadAndInstallApk(
//                                                downloadUrl,
//                                                DEFAULT_DOWNLOAD_PACKAGE_PATH,
//                                                packageLocalPath,
//                                                pendingIntent);
//                                downloadDialog.cancel();
//                            }
//                        },
//                        "取消", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                downloadDialog.cancel();
//                            }
//                        });
//                downloadDialog.setButton1TextColor(getActivity().getResources().getColor(R.color.topbar_background));
//                downloadDialog.show();
//            }
//        });
//    }
//
//    protected Integer getApkPackageVersionCode(Context context, String targetFilePath) {
//        if (!new File(targetFilePath).exists()) {
//            return -1;
//        }
//        PackageManager pm = context.getPackageManager();
//        PackageInfo info = pm.getPackageArchiveInfo(targetFilePath, PackageManager.GET_ACTIVITIES);
//        if (info != null && info.packageName.equals(getStartInfo().appInfo.extension.androidPackage)) {
//            return info.versionCode;
//        }
//        return -1;
//    }
//
//    private void showInstallResult(int installRet) throws RemoteException {
//        String resultString;
//        if (installRet == INSTALL_SUCCESS_RET_CODE) {
//            resultString = "安装成功!";
//        } else {
//            resultString = "插件安装结果:installRet:" + installRet;
//        }
//
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(BaseApplication.getContext(), resultString, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    //检查assets里边内置的apk包的版本
//    private int getAssetsPackageVersion(Activity activity, String pluginApkName, String preInstallFilePath) {
//        int assetsConfigVersion = ZipInstaller.getPreInstallAppVersion(ZipInstaller.getAppFolderName(getStartInfo().appId));
//        if (assetsConfigVersion > 0) {
//            return assetsConfigVersion;
//        }
//        //先检查本地的配置项,如果有配置项,直接返回配置项里的值.减少每次检查都要拷贝包到sd卡,然后才能检查versionCode的消耗
//        //如果没有配置项,则拷贝内置包到sd卡,然后检查versionCode
//        if (copyPluginToDstFolder(activity, pluginApkName, preInstallFilePath)
//                && new File(preInstallFilePath).exists()) {
//            return getApkPackageVersionCode(activity, preInstallFilePath);
//        }
//
//        return -1;
//    }
//
//    private boolean copyPluginToDstFolder(Activity activity, String pluginApkName, String targetFilePath) {
//        Resources res = activity.getResources();
//        String rawFileName = pluginApkName.split("\\.")[0];
//        int id = res.getIdentifier(rawFileName, "raw", activity.getPackageName());
//        //优先使用assets里边的打包的so.lzma, 如果没有从sdCard上找
//        if (id <= 0) {
////            Toast.makeText(activity, "未找到插件apk", Toast.LENGTH_LONG).show();
//            return false;
//        }
//
//        InputStream inputStream = res.openRawResource(id);
//
//        File targetFile = new File(targetFilePath);
//        if (targetFile.exists()) {
//            targetFile.delete();
//        }
//
//        FileUtils.copyFile(inputStream, targetFilePath);
//
//        return true;
//    }
//
//    public ChanjetDialog getDownloadDialog() {
//        return downloadDialog;
//    }
//
//    public String getApkLocalFileName(int appId) {
//        return "nativeplugin_" + appId + ".apk";
//    }
//}

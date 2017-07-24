package com.shenxy.smartapploader.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.shenxy.smartapploader.workTmp.BaseApplication;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

//import com.chanjet.workcircle.util.behavioranalysis.LogToFileUtil;

/**
 * 日志工具类
 * @author shenxy
 *
 */
public class LogUtils {
    private static String TAG = "LogUtils";
	public static boolean sDebug = true;
    public static String TELE_CONFERENCE_TAG="teleConferenceOpen";//电话会议打开
    public static String TELE_CONFERENCE_SOURCE=TAG="teleConferenceOpenSource";//打开电话会议来源
    @SuppressWarnings("unchecked")
    public static String makeLogTag(Class cls) {
        return "Workcircle_" + cls.getSimpleName();
    }

//    public static void V(String sLogTag, String msg) {
//        if (sDebug) {
//            Log.v(sLogTag, msg);
//            //行为统计
//            BehaviorAnalysis.getInstance().Add(BehaviorAnalysis.BEHAVIOR_LOG_I,sLogTag, msg);
//        }
//    }
//
//    public static void V(String sLogTag, String msg, Throwable e) {
//        if (sDebug) {
//            Log.v(sLogTag, msg, e);
//            //行为统计
////            BehaviorAnalysis.getInstance().Add(BehaviorAnalysis.BEHAVIOR_LOG_I, sLogTag, msg, e);
//          //shenxy 2014-4-8 非Error类，不做e的单独打印
//            BehaviorAnalysis.getInstance().Add(BehaviorAnalysis.BEHAVIOR_LOG_I, sLogTag, msg);
//        }
//    }
//
//    public static void D(String sLogTag, String msg) {
//        if (sDebug) {
//            Log.d(sLogTag, msg);
//            //行为统计
//            BehaviorAnalysis.getInstance().Add(BehaviorAnalysis.BEHAVIOR_LOG_D, sLogTag, msg);
//        }
//    }
//
//    public static void D(String sLogTag, String msg, Throwable e) {
//        if (sDebug) {
//            Log.d(sLogTag, msg, e);
//            //行为统计
//            //shenxy 2014-4-8 非Error类，不做e的单独打印
////            BehaviorAnalysis.getInstance().Add(BehaviorAnalysis.BEHAVIOR_LOG_D, sLogTag, msg, e);
//            BehaviorAnalysis.getInstance().Add(BehaviorAnalysis.BEHAVIOR_LOG_D, sLogTag, msg);
//        }
//    }
//
//    public static void I(String sLogTag, String msg) {
//        if (sDebug) {
//            Log.i(sLogTag, msg);
//            //行为统计
//            BehaviorAnalysis.getInstance().Add(BehaviorAnalysis.BEHAVIOR_LOG_I, sLogTag, msg);
//        }
//    }
//
//    public static void I(String sLogTag, String msg, Throwable e) {
//        if (sDebug) {
//            Log.i(sLogTag, msg, e);
//            //行为统计
////            BehaviorAnalysis.getInstance().Add(BehaviorAnalysis.BEHAVIOR_LOG_I, sLogTag, msg, e);
//            //shenxy 2014-4-8 非Error类，不做e的单独打印
//            BehaviorAnalysis.getInstance().Add(BehaviorAnalysis.BEHAVIOR_LOG_I, sLogTag, msg);
//        }
//    }
//
//    public static void W(String sLogTag, String msg) {
//        if (sDebug) {
//            Log.w(sLogTag, msg);
//            //行为统计
//            BehaviorAnalysis.getInstance().Add(BehaviorAnalysis.BEHAVIOR_LOG_W, sLogTag, msg);
//        }
//    }
//
//    public static void W(String sLogTag, String msg, Throwable e) {
//        if (sDebug) {
//            Log.w(sLogTag, msg, e);
//            //行为统计
////            BehaviorAnalysis.getInstance().Add(BehaviorAnalysis.BEHAVIOR_LOG_W, sLogTag, msg, e);
//            //shenxy 2014-4-8 非Error类，不做e的单独打印
//            BehaviorAnalysis.getInstance().Add(BehaviorAnalysis.BEHAVIOR_LOG_W, sLogTag, msg);
//        }
//    }
//
//    public static void E(String sLogTag, String msg) {
//        if (sDebug) {
//            Log.e(sLogTag, msg);
//            //行为统计
//            BehaviorAnalysis.getInstance().Add(BehaviorAnalysis.BEHAVIOR_LOG_E, sLogTag, msg);
//        }
//    }
//
//    public static void E(String sLogTag, String msg, Throwable e) {
//        if (sDebug) {
//            Log.e(sLogTag, msg, e);
//            //行为统计
//            BehaviorAnalysis.getInstance().Add(BehaviorAnalysis.BEHAVIOR_LOG_E, sLogTag, msg, e);
//        }
//    }

    /**
     * 提交到工作圈服务端的日志
     * logHead部分：设备品牌 | 设备名 | 系统版本 | 网络 | 当前用户id | 当前用户name | 占用内存 | 可用内存
     * descriiption部分：类名 | 方法名 | 数据1 | 数据2 | … | 数据N
     *
     * 例如：
     * 三星 | NOTE 2 | 4.0 | wifi模式 | 60000703872 | 18610017024 | 70.171875 | 288.855469  | TWEmployeeDao | queryData | bug-918 | 60000000918
     *
     * @param msg
     */
    public static void T(String classTag, String bugId, String msg, String extraMsg) {
        if (sDebug) {
            try {
                String logHead = getDeviceAndAppInfoString();

                String description = classTag.replace("|", "") + "|" + bugId.replace("|", "") + "|" + msg.replace("|", "");
                description = description + "|" + extraMsg.replace("|", "");

                Log.e("AddWorkcircleLog", logHead + description);
                //保存到文件
//                LogToFileUtil.print(classTag, logHead + description);
//
//                //尝试发送到服务端
//                BehaviorAnalysis.getInstance().AddWorkcircleLog(BehaviorAnalysis.BEHAVIOR_LOG_D, logHead + description);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static String getDeviceAndAppInfoString() {
        ConnectivityManager connectivityManager = (ConnectivityManager) BaseApplication.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return "no connectivityManager";
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        String networkStr;
        if (networkInfo != null) {
            networkStr = NetworkStatusUtil.getCordovaNetworkStatus(networkInfo.getType(), networkInfo.getSubtype()).name();
        } else {
            networkStr = "no network";
        }

        return android.os.Build.MANUFACTURER + "|"
                + android.os.Build.MODEL + "|"
                + android.os.Build.VERSION.RELEASE + "|"
                + networkStr + "|"
//                + AuthInfo.getInstance().getBid() + "|"
//                + AuthInfo.getInstance().getUserName() + "|"
//                + AuthInfo.getInstance().getName() + "|"
                + Runtime.getRuntime().maxMemory() + "|"
                + AppInfoUtil.getVersionName(BaseApplication.getContext()) + "|";
    }

//===============================================================
//    /**
//     * 打点的一级模块
//     */
//    public static enum TeamplusModel {
//        one_workcircle,
//        one_messageList,
//        one_lightAppWork,
//        one_addressbook,
//        one_mine,
//        one_login,
//        one_register,
//        one_specialBiz,
//
//        one_im,         //原有的im相关打点
//        one_regular     //原有的其他打点
//    }
//
//    /**
//     * 圈子模块二级功能，不能超过10个
//     */
//    public static enum TViewWorkCircle {
//        quan_tabClick,
//        quan_search,
//        quan_cardList,
//        quan_cardDetail,
//        quan_createCircle,
//        quan_sweepQR,
//        quan_circleInfo,
//        quan_circleNotCollegeList,
//        quan_QRCode
//    }
//    public static enum TFuncWorkCircle{
//        quan_tabClick,
//        quan_search,
//        quan_cardList,
//        quan_cardDetail,
//        quan_createCircle,
//        quan_sweepQR,
//        quan_circleInfo,
//        quan_circleNotCollegeList,
//        quan_QRCode,
//
//        quan_list_addText,
//        quan_list_addPhoto,
//        quan_list_addImage,
//
//        quan_detail_zan,
//        quan_detail_comment,
//        quan_detail_collect,
//        quan_detail_top,
//        quan_detail_delete,
//        quan_detail_copylink,
//        quan_detail_follow,
//
//        quan_create_finish,
//        quan_create_finishAndAddMember,
//        quan_create_cancel,
//
//        quan_info_edit,
//        quan_info_member,
//        quan_info_confirm,
//        quan_info_addMember,
//
//        quan_addMember_manual,
//        quan_addMember_circleMember,
//        quan_addMember_corpEmployee,
//        quan_addMember_phoneContact,
//        quan_addMember_wechat,
//
//        quan_info_changeAdmin,
//        quan_info_inviteRight,
//        quan_info_exitCircle,
//        quan_info_upgradeCircle,
//        quan_info_deleteCircle,
//
//        quan_QRCode_shareToWeChat,
//        quan_QRCode_save
//    }
//
//    /**
//     * 消息模块二级功能，不能超过10个
//     */
//    public static enum TViewMessageList {
//        msg_tabClick,
//        msg_quanMsg,
//        msg_sysMSg,
//        msg_workMsg,
//        msg_TPlusMsg,
//        msg_createGroupChat,
//        msg_startChat
//    }
//
//    public static enum TFuncMessageList{
//        msg_tabClick,
//        msg_quanMsg,
//        msg_sysMSg,
//        msg_workMsg,
//        msg_TPlusMsg,
//        msg_createGroupChat,
//        msg_startChat,
//
//        msg_quan_cardDetail,
//        msg_wok_workDetail,
//        msg_TPlus_TPlusDetail,
//        msg_create_search,
//        msg_not_disturb_on,
//    }
//
//    public static enum TViewLightAppWork{
//        work_tabClick,
//        work_lightApp
//    }
//    public static enum TFuncLightAppWork{
//        work_tabClick,
//        work_lightApp,
//
//        work_customerMgr_download,
//        work_customerMgr_checkInstall,
//        work_customerMgr_login,
//        work_banner_click,
//    }
//
//    /**
//     * 通讯录模块二级功能，不能超过10个
//     */
//    public static enum TViewAddressBook {
//        addrbook_tabClick,
//        addrbook_switchDepart,
//        addrbook_inviteByCode,
//        addrbook_inviteFromPhoneContacts,
//        addrbook_employInfo
//    }
//
//    public static enum TFuncAddressBook{
//        addrbook_tabClick,
//        addrbook_switchDepart,
//        addrbook_inviteByCode,
//        addrbook_inviteFromPhoneContacts,
//        addrbook_employInfo,
//
//        addrbook_code_changeCode,
//        addrbook_phone_invite,
//        addrbook_phone_search,
//        addrbook_info_saveToPhone,
//        addrbook_info_chat
//    }
//
//    /**
//     * “我的”模块二级功能，不能超过10个
//     */
//    public static enum TViewMine {
//        mine_tabClick,
//        mine_currentCorp,
//        mine_collection,
//        mine_joinCorpByCode,
//        mine_draftbox,
//        mine_changePwd,
//        mine_about,
//        mine_mainPage,
//        mine_exit
//    }
//
//    public static enum TFuncMine{
//        mine_tabClick,
//        mine_currentCorp,
//        mine_collection,
//        mine_joinCorpByCode,
//        mine_draftbox,
//        mine_changePwd,
//        mine_about,
//        mine_mainPage,
//        mine_exit,
//
//        mine_mainPage_detailInfo,
//        mine_mainPage_chat
//    }
//
//    /**
//     * “登录”模块二级功能，不能超过10个
//     */
//    public static enum TViewLogin {
//        login_pageShow,
//        login_loginBtn,
//        login_findPwd
//    }
//
//    public static enum TFuncLogin{
//        login_pageShow,
//        login_loginBtn,
//        login_findPwd
//    }
//
//    /**
//     * “注册”模块二级功能，不能超过10个
//     */
//    public static enum TViewRegister {
//        reg_pageShow,
//        reg_validAccountNext,
//        reg_verifyCodeNext,
//        reg_finish,
//        reg_createCorp
//    }
//    public static enum TFuncRegister{
//        reg_pageShow,
//        reg_validAccountNext,
//        reg_verifyCodeNext,
//        reg_finish,
//        reg_createCorp,
//
//        reg_createCorp_create,
//        reg_createCorp_createSuccess,
//        reg_createCorp_joinByCode,
//        reg_createCorp_joinByInvite
//    }
//
//    /**
//     * "特殊业务统计"的二级功能, 不能超过10个
//     */
//    public static enum TSpecialBiz {
//        special_fist_activity,    //进入主界面后,第一个打开的界面
//        special_invite_type       //创建企业后,邀请方式的点击次数
//    }
//
    public static void LightAppLog(String logParam, String logParamValue){
        G("LightAppOpenCount3", logParam, logParamValue);
//        G(TeamplusModel.one_lightAppWork.name(), logParam, logParamValue);
    }
//
//    /**
//     * 电话会议打开统计
//     * @param logParam
//     * @param logParamValue
//     */
//    public static void TeleConferenceOpenLog(String logParam,String logParamValue){
//        G(TELE_CONFERENCE_TAG,logParam,logParamValue);
//    }
//    public static void workBannerClickLog(String logParamValue){
//        G(TFuncLightAppWork.work_banner_click.name(), "work_banner_click", logParamValue);
//    }
//
//    public static void LoginSplashPageViewLog(int pageIndex){
//        G("loginSplashView", "LoginSplash", pageIndex + "");
//    }
//
//    //shenxy 替换原有im相关打点
//    public static void G_IM(String logParamValue){
////        MobclickAgent.onEvent(BaseApplication.getContext(), logParamValue);
//        G(TeamplusModel.one_im.name(), "im", logParamValue);
//    }
//
//    //shenxy 原有的其他的打点：除了注册漏斗以外的打点，注册漏斗的事件保留
//    public static void G_Old(String logParamValue){
////        MobclickAgent.onEvent(BaseApplication.getContext(), logParamValue);
//        G(TeamplusModel.one_regular.name(), "regular", logParamValue);
//    }
//
    //shenxy 2015-4-20 工作圈的友盟打点整理，map方式折起来打
    private static void G(String logName, String logParamName, String logParamValueName) {
        HashMap<String,String> map = new HashMap<String,String>();
        map.put(logParamName, logParamValueName);

        MobclickAgent.onEvent(BaseApplication.getContext(), logName, map);
    }
//
//    //平铺开，都打在第一级
//    private static void G_flat(String logName, String logParamName, String logParamValueName) {
//        MobclickAgent.onEvent(BaseApplication.getContext(), logParamValueName);
//    }
//
//    //shenxy 圈子模块
//    public static void G_WorkCircle(TViewWorkCircle logParam, TFuncWorkCircle logParamValue){
//        G_flat(TeamplusModel.one_workcircle.name(), logParam.name(), logParamValue.name());
//    }
//
//    //shenxy 消息模块
//    public static void G_MessageList(TViewMessageList logParam, TFuncMessageList logParamValue){
//        G_flat(TeamplusModel.one_messageList.name(), logParam.name(), logParamValue.name());
//    }
//
//    //shenxy 工作模块：动态扩展，不用enum
//    //借用原有LightAppLog方法
//    public static void G_LightAppWork(TViewLightAppWork logParam, TFuncLightAppWork logParamValue){
//        G_flat(TeamplusModel.one_lightAppWork.name(), logParam.name(), logParamValue.name());
//    }
//
//    //shenxy 通讯录模块
//    public static void G_AddressBook(TViewAddressBook logParam, TFuncAddressBook logParamValue){
//        G_flat(TeamplusModel.one_addressbook.name(), logParam.name(), logParamValue.name());
//    }
//
//    //shenxy 我的首页模块
//    public static void G_Mine(TViewMine logParam, TFuncMine logParamValue){
//        G_flat(TeamplusModel.one_mine.name(), logParam.name(), logParamValue.name());
//    }
//
//    //shenxy 登录模块
//    public static void G_Login(TViewLogin logParam, TFuncLogin logParamValue){
//        G_flat(TeamplusModel.one_login.name(), logParam.name(), logParamValue.name());
//    }
//
//    //shenxy 注册模块
//    public static void G_Register(TViewRegister logParam, TFuncRegister logParamValue){
//        G_flat(TeamplusModel.one_register.name(), logParam.name(), logParamValue.name());
//    }
//
//    //对登录状态进入应用主界面以后,点击的第一个界面做统计(用户实际使用的模块)
//    static boolean isFistActivityRecorded;
//    public static void G_SpecialBizFirstActivity(Activity activity) {
//        String activityName = activity.getClass().getSimpleName();
//        if (activityName.equalsIgnoreCase("LoginActivity")) { //每次重新打开,重新计算
//            isFistActivityRecorded = false;
//        }
//
//        if (isFistActivityRecorded) {  //一次启动只记录一次
//            return;
//        }
//
//        if (!BaseApplication.isAppAllreadyStartedAndLogin()) {  //已登录状态
//            return;
//        }
//
//
//        if (activityName.equalsIgnoreCase("MainControllerActivity")  //默认打开的界面忽略
//                || activityName.equalsIgnoreCase("SessionListActivity")) {
//            return;
//        }
//
//        G(TeamplusModel.one_specialBiz.name(), TSpecialBiz.special_fist_activity.name(), activityName);
//        isFistActivityRecorded = true;
//    }
//
//    public static void G_SpecialBizInviteAfterCreateOrg(String inviteTypeName) {
//        G(TeamplusModel.one_specialBiz.name(), TSpecialBiz.special_invite_type.name(), inviteTypeName);
//    }
}

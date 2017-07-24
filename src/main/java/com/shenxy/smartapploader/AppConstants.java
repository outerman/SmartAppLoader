package com.shenxy.smartapploader;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.shenxy.smartapploader.workTmp.BaseApplication;

public class AppConstants {
    public static final String CLIENT_ID_HEAD = "APH";
//
//    private static String host;
//    public static String getHOST() {
//        try {
//            if (StringUtils.isBlank(host)){
//                ApplicationInfo appInfo = BaseApplication.getContext().getPackageManager()
//                        .getApplicationInfo(BaseApplication.getContext().getPackageName(), PackageManager.GET_META_DATA);
//                host = appInfo.metaData.getString("TEAMWORK_HOST");
//            }
//            return host;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
//
//    private static String imHost;
//    public static String getIM_HOST(){
//        try {
//            if (StringUtils.isBlank(imHost)) {
//                ApplicationInfo appInfo = BaseApplication.getContext().getPackageManager()
//                        .getApplicationInfo(BaseApplication.getContext().getPackageName(), PackageManager.GET_META_DATA);
//                imHost = appInfo.metaData.getString("IM_HOST");
//            }
//            return imHost;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
//
//    private static String cspHost;
//    public static String getCSP_HOST(){
//        try {
//            if (StringUtils.isBlank(cspHost)) {
//                ApplicationInfo appInfo = BaseApplication.getContext().getPackageManager()
//                        .getApplicationInfo(BaseApplication.getContext().getPackageName(), PackageManager.GET_META_DATA);
//                cspHost = appInfo.metaData.getString("CSP_HOST");
//            }
//            return cspHost;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
//
//    private static String staticResHost;
//    public static String getStaticRes_HOST(){
//        try {
//            if (StringUtils.isBlank(staticResHost)) {
//                ApplicationInfo appInfo = BaseApplication.getContext().getPackageManager()
//                        .getApplicationInfo(BaseApplication.getContext().getPackageName(), PackageManager.GET_META_DATA);
//                staticResHost = appInfo.metaData.getString("STATIC_RES_HOST");
//            }
//            return staticResHost;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
//
    public static boolean IS_LIGHTAPP_DEBUG() {
        try {
            ApplicationInfo appInfo = BaseApplication.getContext().getPackageManager()
                    .getApplicationInfo(BaseApplication.getContext().getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getBoolean("IS_LIGHTAPP_DEBUG", false);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
//
//    public static String getOneApmToken() {
//        try {
//            ApplicationInfo appInfo = BaseApplication.getContext().getPackageManager()
//                    .getApplicationInfo(BaseApplication.getContext().getPackageName(), PackageManager.GET_META_DATA);
//            return appInfo.metaData.getString("ONEAPM_TOKEN");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
//    }


    public static final String REGEX_EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
    public static final String REGEX_MOBILE_FULL = "^(1[0-9])\\d{9}$";  //全字符是手机号，shenxy 2015-1-4 统一为以1开头的11位数字
    public static final String REGEX_MOBILE = "(1[0-9])\\d{9}";  //含有手机号，shenxy 2015-1-4 统一为以1开头的11位数字
    public static final String REGEX_PHONE = "((\\(\\d{3,4}\\))(\\d{6,8}))|(\\d{3,4}-\\d{6,8})|(\\d{3,17})";
    public static final String REGEX_URL = "((([hH][tT][tT][pP][sS]?)://)|[wW][wW][wW]\\.)"
                                                + "([0-9a-zA-Z_!~*'()-]+\\.)*" // 域名- www.
                                                + "(([0-9a-zA-Z][0-9a-zA-Z-]{0,61})?[0-9a-zA-Z]\\." // 二级s域名
                                                + "[a-zA-Z]{2,6})" // first level domain- .com or .museum
                                                + "(:[0-9]{1,4})?" // 端口- :80
                                                + "(/[0-9a-zA-Z_!~*'().:@&=+$%#-]+)*/?" // a slash isn't required if there is no file name
                                                + "(\\?[0-9a-zA-Z_!~*'().:@&=+$%#-/?]+)?"; //? 后面带参数的情况
    public static final String REGEX_FULL_URL = "(([hH][tT][tT][pP][sS]?)://)([0-9a-zA-Z_!~*'()-]+\\.)*";  //是否是带着协议名的完整url
//    public static final String REGEX_URL = "(([hH][tT][tT][pP][sS]?|[fF][tT][pP]|[fF][iI][lL][eE])://)?"
//                                                + "(([0-9a-zA-Z_!~*'().&=+$%-]+: )?[0-9a-zA-Z_!~*'().&=+$%-]+@)?" //ftp的user@
//                                                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
//                                                + "|" // 允许IP和DOMAIN（域名）
//                                                + "([0-9a-zA-Z_!~*'()-]+\\.)*" // 域名- www.
//                                                + "([0-9a-zA-Z][0-9a-zA-Z-]{0,61})?[0-9a-zA-Z]\\." // 二级域名
//                                                + "[a-zA-Z]{2,6})" // first level domain- .com or .museum
//                                                + "(:[0-9]{1,4})?" // 端口- :80
//                                                + "((/[0-9a-zA-Z_!~*'().:@&=+$%#-]+)?)/?" // a slash isn't required if there is no file name
//                                                + "(\\?[0-9a-zA-Z_!~*'().:@&=+$%#-]+)?"; //? 后面带参数的情况

    //帖子详情链接,纯web链接
    public static final String REGEX_CIRCLE_TOPIC_LINK = "(([hH][tT][tT][pP][sS]?)://)([0-9a-zA-Z]*\\.[0-9a-zA-Z]*\\.[0-9a-zA-Z]*)"
                                                            + "(/[0-9a-zA-Z_!~*'().:@&=+$%#-/]+)*"
                                                            + "topic\\?tid="
                                                            + "[0-9]+";
    //帖子详情链接,转义的正则表达式
    public static final String REGEX_TOPIC_LINK = "\\{\\*([A-Za-z0-9+/=^}]+),([0-9]+)\\}{1}";

    public static final String REGEX_AT = "\\{@([^}]+),([0-9]+)\\}{1}"; //@的正则表达式 {@全体成员,0}sdsd

    public static final String REGEX_TIME = "\\{\\&([A-Za-z0-9+/=^}]+),([A-Za-z0-9+/=^}]+)\\}{1}"; //时间的正则表达式

    public static final String REGEX_VERIFY_CODE = "\\D\\d{4,6}\\D";//短信验证码中，识别连续数字:至少4位,至多6位.

    public static final String REGEX_REPEAT_ACCOUNT = "\\([0-9]+\\)$"; //根据账号，判断账号是否在csp上已经存在重复账号：最后是否有（1）、（2）这样的后缀

    public static final String REGEX_SEARCH_RED = "\\[gzq_m_red\\].*?\\[/gzq_m_red\\]"; //搜索结果中的标红

    public static final boolean HTTP_HEAD_SIG_ENABLE = true; //http请求的头域里，是否添加sig节

    public static final int MAX_SECURITY_CHECK_FAIL_TIMES = 10; //验证码检查失败的最大次数
    public static final int MAX_SECURITY_SEND_TIMES = 10; //验证码发送的最大次数

//    public static final String APPID_T_PLUS = "8f442ec3-713e-4133-b64c-5d2d828c5426";//"0b7dfbbe-e930-4696-b90d-457f0e8e87da";//T+的appid
    public static final String APPID_WORKCIRCLE = "gzq";//"0b7dfbbe-e930-4696-b90d-457f0e8e87da";//T+的appid


    public static final Long T_PLUS_LOCAL_BID = 10086L; //“T+通知”在本地使用的bid，与正式的bid不重复即可
    public static final Long SUBSCRIPTION_LOCAL_BID = 60000001952L;
    public static final Long SYS_LOCAL_BID = 60000001951L;
    public static final Long WORKING_LOCAL_BID = 50000001000L;
    public static final Long T_PLUS_NEW_LOCAL_BID = 50000001001L;

    public static final boolean DEBUG_MEMORY = true; //是否打印内存消耗日志

    //animation set
    public static final boolean ANIMATION_HEADBAR = false;
    public static final boolean ANIMATION_TAB = false;
    public static final boolean ANIMATION_TAB_CONTENT = false;


    //im v3  第三版im 相关
    public static final int GET_MSG_TIME_OUT_IM = 30;//获取消息列表（历史数据）时的超时时间

    public static final int GET_MSG_EMOJI_SIZE = 13; //出现在item-title中的表情的大小
     /**
      * 获取企业默认图标
      */
    private static String lastDefaultOrgLogo = "";
//    public static String getDefaultOrgLogo() {
//        if (TextUtils.isEmpty(lastDefaultOrgLogo)) {
//            lastDefaultOrgLogo = getStaticRes_HOST() + "/static/images/ent_logo_android.png?v=" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH);  //每天换地址即可
//        }
//        return  lastDefaultOrgLogo;
//    }
//    private static String defaultAppIcon="";
//    private static String defaultWorkBannerLink="";
//    /**
//     * crosswalk浏览器内核的下载地址
//     */
    public static final String XWalkCoreDownloadUrl = "http://sto.chanapp.chanjet.com/ecaf1c0b-3794-4a06-864a-17ce0d3245c3/2015/11/02/d2e2a1e3931449e89e503bc7503c499d.lzma";
//
//    /**
//     * 默认 icon地址
//     * @return
//     */
//    public static String getDefaultAppIconUrl(){
//        if(TextUtils.isEmpty(defaultAppIcon)){
//            defaultAppIcon=getStaticRes_HOST() + "/static/images/Icon@2x.png";
//        }
//        return defaultAppIcon;
//    }
//    public static String getExperienceStationUrl(){
//        return getHOST()+"/web/lightapp/guide";
//    }
//    public static String getWorkBannerLink() {
//        if(TextUtils.isEmpty(defaultWorkBannerLink)){
//            defaultWorkBannerLink=getHOST() + "/web/lightapp/headLink";
//        }
//        return defaultWorkBannerLink;
//    }
//
//    public static final String SUCCESS_QUERY_ALERT_PREFIX = "ALERT:"; //发帖和回复接口，成功后如果需要出提示，加上这个前缀
//
    public static final int[] IMAGE_RESOURCES_LOADIND = { R.drawable.tw_chat_refreshing_01,
            R.drawable.tw_chat_refreshing_02, R.drawable.tw_chat_refreshing_03, R.drawable.tw_chat_refreshing_04,
            R.drawable.tw_chat_refreshing_05, R.drawable.tw_chat_refreshing_06, R.drawable.tw_chat_refreshing_07,
            R.drawable.tw_chat_refreshing_08, R.drawable.tw_chat_refreshing_09, R.drawable.tw_chat_refreshing_10,
            R.drawable.tw_chat_refreshing_11, R.drawable.tw_chat_refreshing_12, R.drawable.tw_chat_refreshing_13,
            R.drawable.tw_chat_refreshing_14, R.drawable.tw_chat_refreshing_15, R.drawable.tw_chat_refreshing_16,
            R.drawable.tw_chat_refreshing_17, R.drawable.tw_chat_refreshing_18, R.drawable.tw_chat_refreshing_19,
            R.drawable.tw_chat_refreshing_20, R.drawable.tw_chat_refreshing_21, R.drawable.tw_chat_refreshing_22,
            R.drawable.tw_chat_refreshing_23, R.drawable.tw_chat_refreshing_24, R.drawable.tw_chat_refreshing_25,
            R.drawable.tw_chat_refreshing_26, R.drawable.tw_chat_refreshing_27, R.drawable.tw_chat_refreshing_28};

    public static final int IMAGE_LOADIND_DURATION = 99;


    public static final int REMOTE_ERROR_FAILED_CODE = -200;    //接口没有responseCode
    public static final int REMOTE_ERROR_NULL_CODE = -100;      //接口有responseCode,但是没有转换成目标对象
    public static final int REMOTE_ERROR_FALSE_CODE = -300;      //接口有responseCode,但是业务逻辑中返回false
    public static final int REMOTE_SUCCESS_CODE = 0;            //接口成功的responseCode

    //上传文件类型
    public static final String TYPE_MEDIA_IMAGE="image";
    public static final String TYPE_MEDIA_RAW_IMAGE="raw_image";
    public static final String TYPE_MEDIA_AUDIO="audio";
    public static final String TYPE_MEDIA_VIDEO="video";
    public static final String TYPE_MEDIA_FILE="file";
    public static final String TYPE_MEDIA_LIGHTAPP_IMAGE="lightapp_image";
    public static final String TYPE_MEDIA_LIGHTAPP_FILE="lightapp_file";

    public static final int EVENT_SELECTEMPLOYEE_GROUPCHAT=1;//群聊选人
    public static final int EVENT_SELECTEMPLOYEE_LIGHTAPP=2;//轻应用在通讯录选人

    /**
     * 文件上传子类型
     */
    public static enum UploadSubType{
        test,
        IM_FILE_MSG,//im 文件消息
        IM_IMAGE_MSG,//im 图片消息
    }
}

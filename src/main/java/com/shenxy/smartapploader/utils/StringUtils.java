package com.shenxy.smartapploader.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

//import com.chanjet.workcircle.application.BaseApplication;

/**
 * String工具类
 *
 * @author shenxy
 */
public class StringUtils {
    private static final String LOGTAG = "StringUtils";//LogUtils.makeLogTag(StringUtils.class);
    private static final String ENCODING_UTF8 = "UTF-8";
    private static final char[] QUOTE_ENCODE = "&quot;".toCharArray();
    private static final char[] APOS_ENCODE = "&apos;".toCharArray();
    private static final char[] AMP_ENCODE = "&amp;".toCharArray();
    private static final char[] LT_ENCODE = "&lt;".toCharArray();
    private static final char[] GT_ENCODE = "&gt;".toCharArray();

    /**
     * ***************************************
     * 获取UUID
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * *************************************************
     * 把一个字符串，转换成UTF-8编码的字节数组
     *
     * @param string 需要转换的源字符串
     * @return byte[] 转换后的字节数组
     */
    public static byte[] getUTF8Bytes(String string) {
        if (string == null) {
            return new byte[0];
        }

        try {
            return string.getBytes(ENCODING_UTF8);
        } catch (UnsupportedEncodingException e) {
            /*
             * If system doesn't support UTF-8, use another way
             */
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos);
                dos.writeUTF(string);
                byte[] jdata = bos.toByteArray();
                bos.close();
                dos.close();
                byte[] buff = new byte[jdata.length - 2];
                System.arraycopy(jdata, 2, buff, 0, buff.length);
                return buff;
            } catch (IOException ex) {
                return new byte[0];
            }
        }
    }


    /**
     * *****************************************
     * 把字符串中的通过正则匹配上的字符串替换成其他字符串
     *
     * @param res        : 源字符串
     * @param PatternStr ： 匹配字符串
     * @param replaceStr ： 替换字符串
     * @return String : 返回替换后的字符窜
     */
    public static String replace(String res, String PatternStr, String replaceStr) {
        Pattern p = Pattern.compile(PatternStr);
        Matcher m = p.matcher(res);
        String after = m.replaceAll(replaceStr);
        return after;
    }

    public static String StringFilter(String str) throws PatternSyntaxException {
        // 只允许字母和数字
        // String regEx = "[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        String regEx = "[\"`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+| {}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    // 判断字符串不为空
    public static boolean isNotBlank(String str) {
        if (str != null && !"".equals(str)) {
//        if (str != null && !"".equals(str) && !"null".equals(str)) {
            return true;
        }
        return false;
    }

    // 判断字符串为空
    public static boolean isBlank(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        return false;
    }

    public static String abbreviateMiddle(final String str, final String middle, final int length) {
        if (isBlank(str) || isBlank(middle)) {
            return str;
        }

        if (length >= str.length() || length < middle.length()+2) {
            return str;
        }

        final int targetSting = length-middle.length();
        final int startOffset = targetSting/2+targetSting%2;
        final int endOffset = str.length()-targetSting/2;

        final StringBuilder builder = new StringBuilder(length);
        builder.append(str.substring(0,startOffset));
        builder.append(middle);
        builder.append(str.substring(endOffset));

        return builder.toString();
    }
    /**
     * 获取纯JID（去除JID中的资源ID）
     *
     * @param JID
     * @return
     */
    public static String GetPureJID(String JID) {
        if (isNotBlank(JID)) {
            if (JID.contains("/")) {
                JID = JID.substring(0, JID.indexOf("/"));
            }
        } else {
            return JID;
        }

        return JID;
    }

    /**
     * 获取JID中的resource部分（“/”后面的资源ID）
     *
     * @param JID
     * @return
     */
    public static String GetJIDRecource(String JID) {
        if (isNotBlank(JID)) {
            if (JID.contains("/")) {
                JID = JID.substring(JID.indexOf("/") + 1, JID.length());
            } else {
                return "";
            }
        } else {
            return JID;
        }
        return JID;
    }

    /**
     * 获取Room的JID中的id部分
     *
     * @param JID
     * @return
     */
    public static String GetGroupRoomId(String JID) {
        if (isNotBlank(JID)) {
            if (JID.contains("@")) {
                JID = JID.substring(0, JID.indexOf("@"));
                return JID;
            } else {
                return JID;
            }
        } else {
            return "";
        }
    }

    /**
     * 获取JID中的username部分（“@”前面的用户帐号）
     *
     * @param JID
     * @return
     */
    public static String GetJIDUser(String JID) {
        if (isNotBlank(JID)) {
            if (JID.contains("@")) {
                //shenxy 2014-2-21 区分个人、群组jid的情况
//                if (JID.contains(AppConstants_IM.ROOM_SERVER_DOMAIN)) {
//                    return GetJIDRecource(JID);
//                } else {
                    JID = JID.substring(0, JID.indexOf("@"));
//                }
                return JID;
            } else {
                return JID;
            }
        } else {
            return "";
        }

    }

    /**
     * 获取jid中域名部分（"@后"，“/”前的部分）
     *
     * @param JID
     * @return
     */
    public static String GetJIDDomain(String JID) {
        if (isNotBlank(JID)) {
            JID = GetPureJID(JID);
            if (JID.contains("@")) {
                JID = JID.substring(JID.indexOf("@") + 1, JID.length());
                return JID;
            } else {
                return JID;
            }
        } else {
            return "";
        }
    }

    public static String encodeSpecial(String srcStr) {
        if (srcStr == null) {
            return "";
        }
        return srcStr.replace("\\", "\\\\").replace("\"", "\\\"").replace("\'", "\\\'").replaceAll("\r\n", "").replaceAll("\n", "").replaceAll("\r", "");
    }

    public static String encodeSingleQuotes(String srcStr) {
        if (srcStr == null) {
            return "";
        }
        return srcStr.replace("'", "''");
    }

    public static String encodeDoubleQuotes(String srcStr) {
        if (srcStr == null) {
            return "";
        }
        return srcStr.replace("''", "'");
    }

    /**
     * 采用UTF8编码
     *
     * @param source
     * @return
     */
    public static String encodeUTF8(String source) {
        String dest = "";
        if (source == null || source.equals("")) {
            return "";
        }
        try {
            dest = URLEncoder.encode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return dest;
    }

    /**
     * 对于发送的XML格式内容，进行转义，避免XML关键符号
     * XML保留关键符号共5个：
     * "&", "&amp;"
     * "'", "&apos;"
     * "\"", "&quot;"
     * "<", "&lt;"
     * ">", "&gt;"
     */
//	public static String translateXML(String strXml){
//		//转义时需要最先转&符号，反转义时最后转&符号；
//		//因为当strxml中含有“&apos;”这类字符的时候，先转成“&amp;apos;”
//		return strXml.replace("&", "&amp;").replace("'", "&apos;")
//				.replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;");
//		/**
//		 * 没有应用层的反转义，因为在XmlPullParser中已经自动处理
//		 * 
//		 */
//	}

    /**
     * 对于发送的XML格式内容，进行转义，避免XML关键符号
     * 从Asmack中拷贝出来的，方便引用
     * 没有应用层的反转义，因为在XmlPullParser中已经自动处理
     * <p/>
     * XML保留关键符号共5个：
     * "&", "&amp;"
     * "'", "&apos;"
     * "\"", "&quot;"
     * "<", "&lt;"
     * ">", "&gt;"
     */
    public static String escapeForXML(String string) {
        if (string == null) {
            return null;
        }
        char ch;
        int i = 0;
        int last = 0;
        char[] input = string.toCharArray();
        int len = input.length;
        StringBuilder out = new StringBuilder((int) (len * 1.3));
        for (; i < len; i++) {
            ch = input[i];
            if (ch > '>') {
            } else if (ch == '<') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(LT_ENCODE);
            } else if (ch == '>') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(GT_ENCODE);
            } else if (ch == '&') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                // Do nothing if the string is of the form &#235; (unicode value)
                if (!(len > i + 5 && input[i + 1] == '#' && Character.isDigit(input[i + 2]) && Character.isDigit(input[i + 3]) && Character.isDigit(input[i + 4]) && input[i + 5] == ';')) {
                    last = i + 1;
                    out.append(AMP_ENCODE);
                }
            } else if (ch == '"') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(QUOTE_ENCODE);
            } else if (ch == '\'') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(APOS_ENCODE);
            }
        }
        if (last == 0) {
            return string;
        }
        if (i > last) {
            out.append(input, last, i - last);
        }
        return out.toString();
    }

    //MD5加密，32位
    public static String MD5(String str) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
//
//    /**
//     * 验证是否是手机号
//     *
//     * @param phone
//     * @return
//     */
//    public static boolean isMobilePhone(String phone) {
//        if (!StringUtils.isNotBlank(phone)) {
//            return false;
//        }
//        //扩大范围，免得新号段判断出错
////      Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
//        Pattern p = Pattern.compile(com.chanjet.workcircle.AppUtilConstants.REGEX_MOBILE_FULL);//"^(1[2-8])\\d{9}$");
//        Matcher m = p.matcher(phone);
//        return m.matches();
//    }
//
//    /**
//     * 验证是否是邮箱
//     *
//     * @param mail
//     * @return
//     */
//    public static boolean isMail(String mail) {
//        if (!StringUtils.isNotBlank(mail)) {
//            return false;
//        }
//        // 验证邮箱的正则表达式
//        String format = com.chanjet.workcircle.AppUtilConstants.REGEX_EMAIL;// "\\w{1,15}[@][a-zA-Z0-9]{2,}([.]\\p{Alpha}{2,}){1,2}";
////		String format = "\\p{Alpha}\\w{1,15}[@][a-zA-Z0-9]{2,}([.]\\p{Alpha}{2,}){1,2}";
////		String format = "\\p{Alpha}\\w{2,15}[@][a-z0-9]{3,}[.]\\p{Lower}{2,}";
//        // p{Alpha}:内容是必选的，和字母字符[\p{Lower}\p{Upper}]等价。如：200896@163.com不是合法的。
//        // w{2,15}: 2~15个[a-zA-Z_0-9]字符；w{}内容是必选的。 如：dyh@152.com是合法的。
//        // [a-z0-9]{3,}：至少三个[a-z0-9]字符,[]内的是必选的；如：dyh200896@16.com是不合法的。
//        // [.]:'.'号时必选的； 如：dyh200896@163com是不合法的。
//        // p{Lower}{2,}小写字母，两个以上。如：dyh200896@163.c是不合法的。
//
//        Pattern p = Pattern.compile(format);
//        Matcher m = p.matcher(mail);
//        return m.matches();
//    }

    /**
     * 获取版本号
     *
     * @return
     */
//    public static String GetVersion() {
//        try {
//            Context context = BaseApplication.getContext();
//            PackageManager packageMgr = context.getPackageManager();
//            PackageInfo packInfo;
//            packInfo = packageMgr.getPackageInfo(context.getPackageName(), 0);
//            return packInfo.versionName;
//        } catch (NameNotFoundException e) {
//            LogUtils.E(LOGTAG, "StringUtils GetVersion Error!", e);
//            return "0";
//        }
//
//    }

    /**
     * 获取ip地址
     *
     * @return
     */
    public static String getLocalIpAddress() {
        //此处需要综合判断内网、外网、循环、广播地址等，暂不实现
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();

                    if (!inetAddress.isLoopbackAddress()) {
                        String ipStr = inetAddress.getHostAddress().toString();
                        if (ipStr.contains("%")) {
                            ipStr = ipStr.substring(0, ipStr.indexOf("%"));
                        }
                        return ipStr;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(LOGTAG, "StringUtils getLocalIpAddress Error!", ex);
        }
        return null;
    }

    //调整显示内容，截断、去掉不必要字符
//    public static String GetDisplayContent(Context context, InstantMessageEntity ime) {
//        String tmp = "";
//        if (ime.contentType.equals(AppConstants_IM.TYPE_CONTENT_AUDIO)) {
//            tmp = BaseApplication.GetResourceString("tw_message_text_audio");
////					context.getResources().getString(R.string.tw_message_text_audio);
//        } else if (ime.contentType.equals(AppConstants_IM.TYPE_CONTENT_HTML)) {
//            tmp = BaseApplication.GetResourceString("tw_message_text_html");
////			context.getResources().getString(R.string.tw_message_text_html);
//        } else if (ime.contentType.equals(AppConstants_IM.TYPE_CONTENT_IMAGE)) {
//            tmp = BaseApplication.GetResourceString("tw_message_text_image");
////			context.getResources().getString(R.string.tw_message_text_image);
//        } else if (ime.contentType.equals(AppConstants_IM.TYPE_CONTENT_TEXT)) {
////			tmp = BaseApplication.GetResourceString("tw_message_text_audio");
//            tmp = ime.msg.replace(AppConstants_IM.ORDER_FLAG, "");
//        } else if (ime.contentType.equals(AppConstants_IM.TYPE_CONTENT_VIDEO)) {
//            tmp = BaseApplication.GetResourceString("tw_message_text_video");
////			context.getResources().getString(R.string.tw_message_text_video);
//        }
//
//        return tmp;
//    }

    //返回添加notification时的addingtext
//    public static String GetNotifyAddingContent(Context context, InstantMessageListItem iml) {
//        String tmp = "";
//        InstantMessageEntity ime;
//        if (iml == null) {
//            return tmp;
//        }
//        if (iml.getIm() == null) {
//            return tmp;
//        }
//
//        ime = iml.getIm();
//        if (ime.contentType.equals(AppConstants_IM.TYPE_CONTENT_AUDIO)) {
//            tmp = XmppBaseApplication.GetResourceString("imate_im_notification_adding_multimedia").replace("[name]", iml.getSenderName()).replace("[type]", XmppBaseApplication.GetResourceString("tw_message_text_audio"));
//        } else if (ime.contentType.equals(AppConstants_IM.TYPE_CONTENT_HTML)) {
//            tmp = XmppBaseApplication.GetResourceString("imate_im_notification_adding_multimedia").replace("[name]", iml.getSenderName()).replace("[type]", XmppBaseApplication.GetResourceString("tw_message_text_html"));
//        } else if (ime.contentType.equals(AppConstants_IM.TYPE_CONTENT_IMAGE)) {
//            tmp = XmppBaseApplication.GetResourceString("imate_im_notification_adding_multimedia").replace("[name]", iml.getSenderName()).replace("[type]", XmppBaseApplication.GetResourceString("tw_message_text_image"));
//        } else if (ime.contentType.equals(AppConstants_IM.TYPE_CONTENT_TEXT)) {
//            String msg = iml.getIm().msg;
//            if (msg.length() > 50) {
//                msg = msg.substring(0, 50) + "...";
//            }
//            tmp = XmppBaseApplication.GetResourceString("imate_im_notification_adding").replace("[name]", iml.getSenderName()).replace("[msg]", msg);
//        } else if (ime.contentType.equals(AppConstants_IM.TYPE_CONTENT_VIDEO)) {
//            tmp = XmppBaseApplication.GetResourceString("imate_im_notification_adding_multimedia").replace("[name]", iml.getSenderName()).replace("[type]", XmppBaseApplication.GetResourceString("tw_message_text_video"));
//        }
//
//        return tmp;
//    }

    /**
     * 根据大图的url，获取缩略图的url
     * <src>http://staticossccs.chanjet.com/ecaf1c0b-3794-4a06-864a-17ce0d3245c3/2014/01/04/c7dec1c9e2354f1b9b8bfdc1a1739366.jpg</src>
     *
     * @param src
     * @return
     */
    public static String GetThumbnailImageFromSrc(String src) {
        if (StringUtils.isNotBlank(src)) {
            return src + ".im.jpg";
        } else {
            return "";
        }

    }

    /**
     * 获取版本号
     *
     * @return
     */
    public static String GetVersion(Context context) {
        try {
//			Context context = BaseApplication.getContext();
            PackageManager packageMgr = context.getPackageManager();
            PackageInfo packInfo;
            packInfo = packageMgr.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (NameNotFoundException e) {
            Log.e(LOGTAG, "StringUtils GetVersion Error!", e);
            return "0";
        }

    }

    /**
     * 判断packetID是否是时间戳
     *
     * @return
     */
//    public static boolean IsTimestampPacketID(String packetId) {
//        if (packetId == null || packetId.indexOf("-") > 0) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    /**
//     * 根据联系人的bid返回对应的Jid
//     *
//     * @param bid
//     * @param isGroup 是否群组：true——是群组
//     * @return
//     */
//    public static String GetJidByBid(String bid, boolean isGroup) {
//        if (isGroup) {
//            return bid + "@" + AppConstants_IM.ROOM_SERVER_DOMAIN;
//        } else {
//            return bid + "@" + AppConstants_IM.SERVER_DOMAIN;
//        }
//    }

    public static String getFileNameFromUri(String uri) {
        if (uri != null && uri.length() > 0 ) {
            int pos = uri.lastIndexOf("/");
            if (pos > 0) {
                String fileName = uri.substring(pos);
                return fileName;
            }
        }
        return null;
    }

    public static String GetBid(String JID){
        return JID.substring(0, JID.indexOf("@"));
    }

    public static boolean isNumeric(String numStr){
        if (StringUtils.isBlank(numStr)) {
            return false;
        }
        Pattern p = Pattern.compile("^-?\\d+$");//"^(1[2-8])\\d{9}$");
        Matcher m = p.matcher(numStr);
        return m.matches();
    }
    public static Integer parseInteger(String value){
        if(StringUtils.isBlank(value)){
            return 0;
        }
        try {
            return Integer.parseInt(value);
        }catch (Exception e){
            e.fillInStackTrace();
            return 0;
        }
    }
    public static Long parseLong(String value){
        if(StringUtils.isBlank(value)){
            return 0L;
        }
        try {
            return Long.parseLong(value);
        }catch (Exception e){
            e.fillInStackTrace();
            return 0L;
        }
    }
}

package com.shenxy.smartapploader.utils;

import android.text.style.ClickableSpan;
import android.view.View;

import java.util.HashSet;


public class WeiboURLClickSpan extends ClickableSpan {
//    private Context context;
//    private String URL;
//    private int direction = ExpressionUtil2.DEFAULT_DIRECTION;  //所在聊天的方向

    static HashSet<String> downloadFileExtendName = new HashSet<>();
    static {
        downloadFileExtendName.clear();
        downloadFileExtendName.add(".rar");
        downloadFileExtendName.add(".apk");
        downloadFileExtendName.add(".zip");
        downloadFileExtendName.add(".001");
        downloadFileExtendName.add(".002");
        downloadFileExtendName.add(".003");
        downloadFileExtendName.add(".004");
        downloadFileExtendName.add(".005");
        downloadFileExtendName.add(".006");
        downloadFileExtendName.add(".007");
        downloadFileExtendName.add(".008");
        downloadFileExtendName.add(".009");
        downloadFileExtendName.add(".pdf");
        downloadFileExtendName.add(".pdg");
        downloadFileExtendName.add(".dat");
        downloadFileExtendName.add(".db");
        downloadFileExtendName.add(".doc");
        downloadFileExtendName.add(".mp3");
        downloadFileExtendName.add(".r01");
        downloadFileExtendName.add(".r02");
        downloadFileExtendName.add(".r03");
        downloadFileExtendName.add(".r04");
        downloadFileExtendName.add(".r05");
        downloadFileExtendName.add(".r06");
        downloadFileExtendName.add(".r07");
        downloadFileExtendName.add(".r08");
        downloadFileExtendName.add(".r09");
        downloadFileExtendName.add(".ppt");
        downloadFileExtendName.add(".xls");
        downloadFileExtendName.add(".xlsx");
        downloadFileExtendName.add(".pptx");
        downloadFileExtendName.add(".docx");
        downloadFileExtendName.add(".chm");
        downloadFileExtendName.add(".exe");
    }

//    public WeiboURLClickSpan(Context context, String group) {
//        this.context = context;
//        this.URL = group;
//    }

//    public WeiboURLClickSpan(Context context, String group, int direction) {
//        this.context = context;
//        this.URL = group;
//        this.direction = direction;
//    }

//    @Override
//    public void onClick(View arg0) {
//        if (TextUtils.isEmpty(URL)) {
//            return;
//        }
//
//        String openUrl = URL;
//        Matcher urlMatcher = Pattern.compile(AppConstants.REGEX_FULL_URL).matcher(openUrl);
//        if (!urlMatcher.find()) {
//            openUrl = "http://" + openUrl;
//        }
//
//        Matcher topicLinkMatcher = Pattern.compile(AppConstants.REGEX_CIRCLE_TOPIC_LINK).matcher(openUrl);
//        if (topicLinkMatcher.find()) {
//            String topicId = openUrl.substring(openUrl.indexOf("tid=") + 4);
//
//            Intent intent = new Intent();
//            intent.setClass(context, CardDetailRxActivity.class);
//            intent.putExtra("topicId", topicId);
//            intent.putExtra("topicFrom", CardDetailRxActivity.TopicFromType.fromActivityStarter.ordinal());
////            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            context.startActivity(intent);
//        }
//        else if (isOpenInSystemBroswer(openUrl)) {//如果是下载文件，则弹出新的系统自带浏览器进行下载
//            try {
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(openUrl));
//                context.startActivity(intent);
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        else {
//            openUrlInAppMainActivity(context, openUrl);
//        }
//    }

    //是否使用系统浏览器打开链接
    public static boolean isOpenInSystemBroswer(String url) {
        if (!url.contains(".")) {
            return false;
        }
        String extName = url.substring(url.lastIndexOf("."));
        return downloadFileExtendName.contains(extName);
    }

    @Override
    public void onClick(View widget) {

    }

//    public static void openUrlInAppMainActivity(Context context, String url){
//        String[] data=new String[5];
//        data=new String[5];
//        data[0]= AppMainViewModel.TYPE_SHARE;
//        data[1]="";
//        data[2]="";
//        data[3]= url;
//        data[4]= AppConstants.getDefaultAppIconUrl();
//
//        LightAppManager.getInstance().startRemoteUrlLightApp((Activity) context, url, "",data, AppMainViewModel.SHOW_TITLEBAR);
//    }
//
//    @Override
//    public void updateDrawState(TextPaint ds) {
//        if (ds == null) {
//            return;
//        }
//
//        ds.setColor(ExpressionUtil2.getSpanColor(direction));
//        ds.setUnderlineText(false);//去掉下划线
//    }

}

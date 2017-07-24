//package com.shenxy.smartapploader.framework.installer;
//
//
//import com.shenxy.smartapploader.framework.LightAppConstants;
//import com.shenxy.smartapploader.request.MyWorkQuery;
//import com.shenxy.smartapploader.workTmp.OrgManager;
//
///**
// * Created by shenxy on 16/5/10.
// * 隐藏app的工具类
// */
//public class HideAppInstaller {
//    public static final int STATUS_LOCAL = -1111;
//
//    public MyWorkQuery.MyWorkItem makeHideAppInfo() {
//        MyWorkQuery.MyWorkItem workItem = new MyWorkQuery.MyWorkItem();
//        workItem.appId = LightAppConstants.SHOW_HIDE_APP;
//        workItem._id = String.valueOf(LightAppConstants.SHOW_HIDE_APP);
//        workItem.status = STATUS_LOCAL;  //本地
//        workItem.name = "更多";
//        workItem.orgId = OrgManager.getInstance().getCurrentOrgInfo() == null ? "0" : OrgManager.getInstance().getCurrentOrgInfo().orgId;
//        workItem.iconUrl = "lightapp_more";
//        workItem.appType = LightAppConstants.DEFAULT_APP_TYPE;
//        workItem.version = Integer.MAX_VALUE;
//
//        updateUnreadAndAuditNum(workItem);
//
//        return workItem;
//    }
//
//    public static void updateUnreadAndAuditNum(MyWorkQuery.MyWorkItem workItem) {
////        try {
////            List<MyWorkQuery.MyWorkItem> workItems = OrgManager.getInstance().getWorkItems();
////            if (workItems == null) {
////                return;
////            }
////
////            workItem.unReadNum = 0;
////            workItem.auditNum = 0;
////            for (MyWorkQuery.MyWorkItem item : workItems) {
////                if (LightAppSortModel.getInstance().isHideApp(item.appId)) {
////                    workItem.unReadNum += item.unReadNum;
////                    workItem.auditNum += item.auditNum;
////                }
////            }
////        }
////        catch (Exception e) {
////            e.printStackTrace();
////        }
//    }
//
//}

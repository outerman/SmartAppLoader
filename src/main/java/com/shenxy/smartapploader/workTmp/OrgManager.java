//package com.shenxy.smartapploader.workTmp;
//
//import com.shenxy.smartapploader.utils.StringUtils;
//
///**
// * 企业相关
// * Created by jiasta on 2015/3/3.
// */
//public class OrgManager {//implements SingletonFinalizer{
//    private static OrgManager instance;
//    private OrgListQuery.OrgInfo currentOrgInfo;
//
//    public static OrgManager getInstance(){
//        if (instance == null){
//            instance = new OrgManager();
////            BaseApplication.context.addObject(instance);
//
//        }
//        return instance;
//    }
//
//    /**
//     * 读取当前企业
//     */
//    public OrgListQuery.OrgInfo getCurrentOrgInfo() {
//
//        return currentOrgInfo;
//    }
//
//    /**
//     * 获取当前企业id
//     * @return
//     */
//    public String getCurOrgId(){
//         if(getCurrentOrgInfo()==null){
//           return null;
//         }
//        if(StringUtils.isBlank(getCurrentOrgInfo().orgId)){
//            return null;
//        }
//        return OrgManager.getInstance().getCurrentOrgInfo().orgId;
//    }
//}

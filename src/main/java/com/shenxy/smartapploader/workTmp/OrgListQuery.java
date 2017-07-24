//package com.shenxy.smartapploader.workTmp;
//
//import com.shenxy.smartapploader.request.BaseResponse;
//
//import java.io.Serializable;
//import java.util.List;
//
///**
// * Created by shenxy 2014-9-16
// * 企业列表查询
// */
//public class OrgListQuery {
//
//    public static class Request {
///*        public String interUrl = AppConstants_IM.API_BSS_HOST + AppConstants_IM.BSS_GET_USER;
//        public String methodType = "get";
//
//        public String noNeedUserInfo;
//        public String needOrgLists;*/
////        public String deptId;
////        public int pageNo;
////        public int pageSize;
//        public  String str="";//无参会报错 暂时随便写一个
//
//    }
//
//    public static class Response extends BaseResponse {
////        public Integer code;
////        public String msg;
//        public List<OrgInfo> data;
//    }
//
///*    public static class Response extends BaseResponse {
//        public Integer code;
//        public String msg;
//        public Data data;
//    }
//
//    public static class Data{
//        public OrgListInfo orgListInfo;
////        public String orgListInfo;
//    }
//    public static class OrgListInfo{
//        public String appDefaultOrgId;
//        public List<OrgInfo> orgList;//todo 应该返回企业所有信息
//    }*/
//
//    /**
//     * result为true，代表操作成功,如果result为true时，有：
//     employeeInfo：通讯录中的员工信息；
//     deptId:部门ID
//     deptName：部门名称
//     email:邮箱
//     empId:员工主键
//     isBelongCurrentEnt：是否加入企业(0未加入，1加入)
//     isDeptAdmin：是否为部门主管(0不是，1是)
//     isOrgAdmin：是否为企业管理员(0不是，1是)
//     mobile：手机
//     name：姓名
//     position：职位
//     pageInfo：分页相关信息
//     pageNo:当前页数
//     pageNum:总页数
//     pageSize:每页记录数
//     totalRecords:总记录数
//
//     */
//    public static class OrgInfo implements Serializable {
//
//        public String orgId;//
//        public String orgLogo;//
//        public String isInitial = "0";//shenxy 2015-4-8 考虑-9000的假企业，默认为0——未初始化
//        public String orgName;//
//        public int unReadNum;//
//        public int auditNum;//
//        public String orgFullName;//
//
//        //shenxy 2014-12-23 增加当前企业的员工数
//        public int employeeNum;
//
//        public String isOrgManager = ""; //shenxy 2015-3-5 是否管理员
//
///*        public String address;//
//        public String email;//
//        public String emailBinding;//
//        public String employeeNum;//
//        public String enterpriseType;//
//        public String fax;//
//        public String industry;//
//        public String isInitial;//
//        public String legalPerson;//
//        public String mobile;//
//        public String mobileBinding;//
//        public String orgAccount;//
//
//        public String orgChanjetId;//
//        public String orgDomain;//
//        public String orgFullName;//
//        public String orgId;//
//        public String orgLogo;//
//        public String orgName;//
//        public String orgType;//
//        public String phone;//
//        public String website;//
//
//        public boolean hasNewMsg;//是否有新消息 （业务需要，自己添加的属性）*/
//
//    }
//
//
////    @Override
////    protected Class<Request> getRequestClassType() {
////        return Request.class;
////    }
////
////    @Override
////    protected Class<Response> getResponseClassType() {
////        return Response.class;
////    }
////
////    public OrgListQuery() {
////        setRequestMethod(HTTPRequest.Method.GET);
////        setUrlPattern(AppConstants.getHOST() + AppConstants.API_FIND_USER_ORGS_NUM);
////    }
//
//}

package com.api.finance.search.web;

import com.api.finance.entity.CommentReview;
import com.api.finance.entity.Review;
import com.api.finance.search.service.FinanceService;
import com.api.finance.search.service.impl.FinanceServiceImpl;
import com.api.finance.util.ConstantInterface;
import com.engine.common.util.ParamUtil;
import com.engine.common.util.ServiceUtil;
import com.weaverboot.frame.ioc.anno.parameterAnno.WeaParamBean;
import net.minidev.json.JSONObject;
import oracle.jdbc.proxy.annotation.Post;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/finance")
public class FinanceAction {

    private FinanceService getService(User user) {
        return ServiceUtil.getService(FinanceServiceImpl.class, user);
    }

    /**
     * 获取Tab列表数据
     * @param request
     * @param response
     * @return
     */
    @GET
    @Path("/tab")
    @Produces({MediaType.TEXT_PLAIN})
    public String getTabList(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> apidatas = new HashMap<String,Object>();
        try {
            //获取当前用户
            User user = HrmUserVarify.getUser(request, response);
            apidatas = getService(user).getTadList(ParamUtil.request2Map(request),user);
            apidatas.put("api_status", true);
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("api_status", false);
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * 获取列表数据
     * @param request
     * @param response
     * @return
     */
    @GET
    @Path("/user/authority")
    @Produces({MediaType.TEXT_PLAIN})
    public String getUserAuthority(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> apidatas = new HashMap<>();
        try {
            //获取当前用户
            User user = HrmUserVarify.getUser(request, response);
            boolean isAdmin = getService(user).getRoleByHrmId(ConstantInterface.ROLE_ID, user.getUID());
            Map<String, Object> categoryManager = getService(user).getCategoryManager(user.getUID());
            boolean isCategoryManaer = (boolean) categoryManager.get("isCategoryManager");
            List<Integer> category = (List<Integer>) categoryManager.get("category");
            List<String> ml = new ArrayList<>();
            for (Integer cate : category) {
                String categoryId = getService(user).getTabSqlIdsMap(cate.toString());
                ml.add(categoryId);
            }
            apidatas.put("isCategoryManager", isCategoryManaer);
            apidatas.put("category", ml);
            apidatas.put("isAdmin", isAdmin);
            apidatas.put("api_status", true);
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("api_status", false);
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * 获取列表数据
     * @param request
     * @param response
     * @return
     */
    @GET
    @Path("/list")
    @Produces({MediaType.TEXT_PLAIN})
    public String getList(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> apidatas = new HashMap<String,Object>();
        try {
            //获取当前用户
            User user = HrmUserVarify.getUser(request, response);
            apidatas = getService(user).getFinanceList(ParamUtil.request2Map(request),user);
            //int roleId = getService(user).getRoleIdByRoleName("");
            boolean isAdmin = getService(user).getRoleByHrmId(ConstantInterface.ROLE_ID, user.getUID());
            Map<String, Object> categoryManager = getService(user).getCategoryManager(user.getUID());
            apidatas.put("categoryManager", categoryManager);
            apidatas.put("isAdmin", isAdmin);
            apidatas.put("api_status", true);
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("api_status", false);
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * 流程跳转地址
     * @param request
     * @param response
     * @return
     */
    @GET
    @Path("/workflow")
    @Produces({MediaType.TEXT_PLAIN})
    public String getWorkFlowAddress(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> apidatas = new HashMap<String,Object>();
        try {
            //获取当前用户
            User user = HrmUserVarify.getUser(request, response);
            apidatas = getService(user).getWorkflowAddress(ParamUtil.request2Map(request),user);
            apidatas.put("api_status", true);
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("api_status", false);
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * 废止操作
     * @param request
     * @param response
     * @return
     */
    @GET
    @Path("/abolish")
    @Produces({MediaType.TEXT_PLAIN})
    public String abolish(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> apidatas = new HashMap<String,Object>();
        try {
            //获取当前用户
            User user = HrmUserVarify.getUser(request, response);
            boolean abolish = getService(user).abolish(ParamUtil.request2Map(request), user);
            apidatas.put("api_status", abolish);
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("api_status", false);
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * 财经学院
     * @param request
     * @param response
     * @return
     */
    @GET
    @Path("/economics")
    @Produces({MediaType.TEXT_PLAIN})
    public String economics(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> apidatas = new HashMap<>();
        try {
            //获取当前用户
            User user = HrmUserVarify.getUser(request, response);
            apidatas = getService(user).getEconomics(ParamUtil.request2Map(request), user);
            apidatas.put("api_status", true);
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("api_status", false);
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }


    /**
     * 保存财经学院
     * @param request
     * @param response
     * @return
     */
    @POST
    @Path("/save/economics")
    @Produces({MediaType.TEXT_PLAIN})
    public String saveEconomics(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> apidatas = new HashMap<>();
        try {
            //获取当前用户
            User user = HrmUserVarify.getUser(request, response);
            boolean result = getService(user).saveEconomics(ParamUtil.request2Map(request), user);
            apidatas.put("api_status", result);
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("api_status", false);
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * 更新财经学院
     * @param request
     * @param response
     * @return
     */
    @POST
    @Path("/update/economics")
    @Produces({MediaType.TEXT_PLAIN})
    public String updateEconomics(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> apidatas = new HashMap<>();
        try {
            //获取当前用户
            User user = HrmUserVarify.getUser(request, response);
            boolean result = getService(user).updateEconomics(ParamUtil.request2Map(request), user);
            apidatas.put("api_status", result);
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("api_status", false);
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }



    /**
     * 解读查询
     * @param request
     * @param response
     * @return
     */
    @GET
    @Path("/interpretation")
    @Produces({MediaType.TEXT_PLAIN})
    public String interpretation(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> apidatas = new HashMap<>();
        try {
            //获取当前用户
            User user = HrmUserVarify.getUser(request, response);
            apidatas = getService(user).getInterpretation(ParamUtil.request2Map(request), user);
            apidatas.put("api_status", true);
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("api_status", false);
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }


    /**
     * 保存保存解读
     * @param request
     * @param response
     * @return
     */
    @POST
    @Path("/save/interpretation")
    @Produces({MediaType.TEXT_PLAIN})
    public String saveInterpretation(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> apidatas = new HashMap<>();
        try {
            //获取当前用户
            User user = HrmUserVarify.getUser(request, response);
            boolean result = getService(user).saveInterpretation(ParamUtil.request2Map(request), user);
            apidatas.put("api_status", result);
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("api_status", false);
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * 更新解读
     * @param request
     * @param response
     * @return
     */
    @POST
    @Path("/update/interpretation")
    @Produces({MediaType.TEXT_PLAIN})
    public String updateInterpretation(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> apidatas = new HashMap<>();
        try {
            //获取当前用户
            User user = HrmUserVarify.getUser(request, response);
            boolean result = getService(user).updateInterpretation(ParamUtil.request2Map(request), user);
            apidatas.put("api_status", result);
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("api_status", false);
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }


    /**
     * 制度评论列表
     * @param request
     * @param response
     * @return
     */
    @GET
    @Path("/review")
    @Produces({MediaType.TEXT_PLAIN})
    public String review(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> apidatas = new HashMap<>();
        try {
            //获取当前用户
            User user = HrmUserVarify.getUser(request, response);
            apidatas = getService(user).getReviewList(ParamUtil.request2Map(request), user);
            apidatas.put("api_status", true);
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("api_status", false);
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * 制度评论状态更新
     * @param request
     * @param response
     * @return
     */
    @GET
    @Path("/review/status")
    @Produces({MediaType.TEXT_PLAIN})
    public String updateStatus(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> apidatas = new HashMap<>();
        try {
            //获取当前用户
            User user = HrmUserVarify.getUser(request, response);
            boolean result = getService(user).updateReviewStatusById(ParamUtil.request2Map(request), user);
            apidatas.put("api_status", result);
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("api_status", false);
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * 保存评论内容
     * @param request
     * @param response
     * @return
     */
    @POST
    @Path("/save/review")
    @Produces({MediaType.TEXT_PLAIN})
    public String saveReview(@Context HttpServletRequest request, @Context HttpServletResponse response, @WeaParamBean Review review){
        Map<String,Object> apidatas = new HashMap<>();
        try {
            //获取当前用户
            User user = HrmUserVarify.getUser(request, response);
            boolean result = getService(user).saveReview(ParamUtil.request2Map(request), user, review);
            apidatas.put("api_status", result);
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("api_status", false);
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * 根据docid制度评论列表
     * @param request
     * @param response
     * @return
     */
    @GET
    @Path("/single/review")
    @Produces({MediaType.TEXT_PLAIN})
    public String getReviewByDocid(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> apidatas = new HashMap<>();
        try {
            //获取当前用户
            User user = HrmUserVarify.getUser(request, response);
            apidatas = getService(user).getReviewListByDocid(ParamUtil.request2Map(request), user);
            apidatas.put("api_status", true);
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("api_status", false);
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }


    /**
     * 保存评论回复内容
     * @param request
     * @param response
     * @return
     */
    @POST
    @Path("/save/comment/review")
    @Produces({MediaType.TEXT_PLAIN})
    public String saveCommentReview(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> apidatas = new HashMap<>();
        try {
            //获取当前用户
            User user = HrmUserVarify.getUser(request, response);
            boolean result = getService(user).saveCommentReview(ParamUtil.request2Map(request), user);
            apidatas.put("api_status", result);
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("api_status", false);
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * 根据评论id获取回复内容
     * @param request
     * @param response
     * @return
     */
    @GET
    @Path("/get/comment")
    @Produces({MediaType.TEXT_PLAIN})
    public String getCommentByReviewId(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> apidatas = new HashMap<>();
        try {
            //获取当前用户
            User user = HrmUserVarify.getUser(request, response);
            Map<String, Object> params = ParamUtil.request2Map(request);
            Object plid = params.get("plid");
            CommentReview comment = getService(user).getCommentReviewByReviewId(plid);
            apidatas.put("datas", comment);
            apidatas.put("api_status", true);
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("api_status", false);
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * 根据评论id获取回复内容
     * @param request
     * @param response
     * @return
     */
    @GET
    @Path("/message")
    @Produces({MediaType.TEXT_PLAIN})
    public String message(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Map<String,Object> apidatas = new HashMap<>();
        try {
            //获取当前用户
            User user = HrmUserVarify.getUser(request, response);
            Map<String, Object> params = ParamUtil.request2Map(request);
            getService(user).message(params, user);
            apidatas.put("api_status", true);
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("api_status", false);
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

}

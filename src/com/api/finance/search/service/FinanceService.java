package com.api.finance.search.service;

import com.api.finance.entity.CommentReview;
import com.api.finance.entity.Review;
import weaver.hrm.User;

import java.util.Map;

/**
 *
 */
public interface FinanceService {

     String getTabSqlIdsMap(String key);
    /**
     * 根据用户id，判断是不是目录管理员，以及获取管理的目录表
     *
     * @param resourceId
     * @return
     */
    Map<String, Object> getCategoryManager(int resourceId);

    /**
     * 判断角色下有没有当前用户
     *
     * @param roleId
     * @param resourceId
     * @return
     */
    boolean getRoleByHrmId(int roleId, int resourceId);

    /**
     * 根据角色名称查询角色id
     *
     * @param name
     * @return
     */
    int getRoleIdByRoleName(String name);

    /**
     * 获取Tap列表
     *
     * @param params
     * @param user
     * @return
     */
    Map<String, Object> getTadList(Map<String, Object> params, User user);

    /**
     * 获取制度列表
     *
     * @param params
     * @param user
     * @return
     */
    Map<String, Object> getFinanceList(Map<String, Object> params, User user);


    /**
     * 获取当前登录人可发布的制度公告流程地址
     *
     * @param params
     * @param user
     * @return
     */
    Map<String, Object> getWorkflowAddress(Map<String, Object> params, User user);


    /**
     * 废止操作
     *
     * @param
     */
    boolean abolish(Map<String, Object> params, User user);


    /**
     * 财经学院查询
     *
     * @param params
     * @param user
     * @return
     */
    Map<String, Object> getEconomics(Map<String, Object> params, User user);

    /**
     * 保存财经学院内容
     *
     * @param params
     * @param user
     * @return
     */
    boolean saveEconomics(Map<String, Object> params, User user);

    /**
     * 更新财经学院内容
     *
     * @param params
     * @param user
     * @return
     */
    boolean updateEconomics(Map<String, Object> params, User user);


    /**
     * 解读
     *
     * @param params
     * @param user
     * @return
     */
    Map<String, Object> getInterpretation(Map<String, Object> params, User user);

    /**
     * 保存财经学院内容
     *
     * @param params
     * @param user
     * @return
     */
    boolean saveInterpretation(Map<String, Object> params, User user);

    /**
     * 更新财经学院内容
     *
     * @param params
     * @param user
     * @return
     */
    boolean updateInterpretation(Map<String, Object> params, User user);

    /**
     * 获取制度评论列表
     *
     * @param params
     * @param user
     * @return
     */
    Map<String, Object> getReviewList(Map<String, Object> params, User user);


    /**
     * 保存制度评论内容
     *
     * @param params 评论表数据
     * @param user   当前用户
     * @return
     */
    boolean saveReview(Map<String, Object> params, User user, Review review);

    /**
     * 获取单个制度评论列表
     *
     * @param params
     * @param user
     * @return
     */
    Map<String, Object> getReviewListByDocid(Map<String, Object> params, User user);

    /**
     * 更新制度评论状态
     *
     * @param params 评论表数据
     * @param user   当前用户
     * @return
     */
    boolean updateReviewStatusById(Map<String, Object> params, User user);

    /**
     * 更新制度评论回复状态
     *
     * @param id   评论id
     * @param hfzt 回复状态
     * @return
     */
    boolean updateReviewReplyStatusById(Object id, Integer hfzt);


    /**
     * 保存制度评论回复内容
     *
     * @param params 评论表数据
     * @param user   当前用户
     * @return
     */
    boolean saveCommentReview(Map<String, Object> params, User user);


    /**
     * 根据评论id查询回复内容
     *
     * @param plid 评论id
     * @return
     */
    CommentReview getCommentReviewByReviewId(Object plid);

    /**
     * 消息通知
     * @param params
     * @param user
     */
     void message(Map<String, Object> params, User user);

    /**
     * 给目录管理员发送消息
     * @param params
     * @param user
     */
    void message2CategoryManager(Map<String, Object> params, User user);
}

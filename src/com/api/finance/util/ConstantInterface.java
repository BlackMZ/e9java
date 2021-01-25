package com.api.finance.util;

/**
 * @author Arlo
 * @version 1.0.0
 * @Description 财务通用字段
 * @ClassName ConstantInterface.java
 * @createTime 2021年01月05日 16:35:00
 */
public interface ConstantInterface {
    /**
     * 父级目录id
     */
//    Integer PARENT_ID = 199;
    //生产环境使用
    Integer PARENT_ID = 201;


    /**
     * 文档目录管理员角色id
     */
//    Integer ROLE_ID = 32;
    //生产环境角色id
    Integer ROLE_ID = 34;


    /**
     *  消息类型 财务评论
     */
//    int MESSAGE_TO_CATEGORY_TYPE = 1166;
    Integer MESSAGE_TO_CATEGORY_TYPE = 1275;//生产环境


    /**
     *  消息类型 新制度发布
     */
//    int MESSAGE_TO_RESOURCE = 1230;
    Integer MESSAGE_TO_RESOURCE = 1335;//生产环境

    /**
     * 发文流程对应组织分部和流程id
     *
     * 地产 正荣地产
     */
    String REAL_ESTATE = "正荣地产";
    String ESTATE_WORKFLOW = "59";
    /**
     * 集团 集团总部
     */
    String GROUP_HEADQUARTERS = "集团总部";
    String GROUP_WORKFLOW = "61";

    /**
     * 服务 服务总部
     */
    String SERVICE_HEADQUARTERS = "服务总部";
    String SERVICE_WORKFLOW = "62";

    /**
     * 商业 商管子公司
     */
    String SUB_COMPANY = "商管子公司";
    String SUB_COM_WORKFLOW = "63";
    /**
     * 物业 物业区域
     */
    String PROPERTY_AREA = "物业区域";
    String AREA_WORKFLOW = "65";


    /**
     *  消息类型 财务评论回复
     */
    int MESSAGE_TYPE = 1147;
    //Integer MESSAGE_TYPE = 1197;//生产环境
}

package weaver.workflow.action;

import com.api.finance.util.ConstantInterface;
import com.cloudstore.dev.api.bean.MessageBean;
import com.cloudstore.dev.api.bean.MessageType;
import com.cloudstore.dev.api.util.Util_Message;
import org.apache.commons.lang3.ObjectUtils;
import weaver.conn.RecordSet;
import weaver.formmode.exttools.impexp.common.DateUtils;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
import weaver.workflow.workflow.WorkflowComInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Arlo
 * @version 1.0.0
 * @Description  根据不同人力资源条件推送消息
 * @ClassName SendMessageToResource.java
 * @CreateTime 2021年01月17日 11:04:00
 */
public class SendMessageToResource extends BaseBean implements Action {

    @Override
    public String execute(RequestInfo requestInfo) {
        WorkflowComInfo workflowComInfo = new WorkflowComInfo();
        String workflowId = requestInfo.getWorkflowid();
        new BaseBean().writeLog("=====非制度公告发布workflowid===============", workflowId);
        String requestId = requestInfo.getRequestid();
        new BaseBean().writeLog("=====非制度公告发布requestid===============", requestId);
        String isBill = workflowComInfo.getIsBill("" + workflowId);
        //表单Id
        int formId = weaver.general.Util.getIntValue(workflowComInfo.getFormId("" + workflowId));
        //表名
        String tableName = getTableName(formId, Util.getIntValue(isBill));
        new BaseBean().writeLog("=====非制度公告发布Name===============", tableName);
        RecordSet recordSet = new RecordSet();
        recordSet.executeQuery("select ckfw, gwbt, zwnr, fbr from " + tableName + " where requestid = " + requestId);
        //查看范围
        String viewRange = "";
        String gwbt = "";
        String zwnr = "";
        String fbr = "";
        if (recordSet.next()) {
            viewRange = recordSet.getString("ckfw");
            gwbt = recordSet.getString("gwbt");
            zwnr = recordSet.getString("zwnr");
            fbr = recordSet.getString("fbr");
        }
        Set<String> receivers = getResourceId(viewRange);
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("title", gwbt);
        messageMap.put("receiver", receivers);
        messageMap.put("creator", fbr);
        messageMap.put("docId", zwnr);
        StringBuilder sb = new StringBuilder();
        String sql = "select hrm.lastname from HrmResource hrm  where hrm.id = " + fbr;
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql);
        if (rs.next()) {
            sb.append("创建人：");
            sb.append(rs.getString("lastname"));
            sb.append("<br/>");
        }
        sb.append("创建时间：");
        sb.append(DateUtils.getCurrentDateTime());
        messageMap.put("context", sb);
        message2Resource(messageMap);
        return Action.SUCCESS;
    }

    private Set<String> getResourceId(String viewRange) {
        Set<String> receivers = new HashSet<>();
        //1人员
        Set<String> hrmIdList = new HashSet<>();
        //2 分部
        Set<String> subCompanyIdList = new HashSet<>();
        //3 部门 6065
        Set<String> departmentIdList = new HashSet<>();
        //4 角色
        Set<String> roleIdList = new HashSet<>();
        //5 所有人 是安全级别
        Set<String> allIdList = new HashSet<>();
        //6 岗位
        Set<String> jobTitleIdList = new HashSet<>();

        String[] resourceList = viewRange.split("~");
        for (String role : resourceList) {
            String[] hrmResource = role.split("_");
            if (role.startsWith("1_")) {
                //人员
                hrmIdList.add(hrmResource[1]);
            } else if (role.startsWith("2_")) {
                //分部
                subCompanyIdList.add(hrmResource[1]);
            } else if (role.startsWith("3_")) {
                //部门
                departmentIdList.add(hrmResource[1]);
            } else if (role.startsWith("4_")) {
                // 角色
                roleIdList.add(hrmResource[1]);
            } else if (role.startsWith("5_")) {
                // 所有人
                allIdList.add(hrmResource[1]);
            } else if (role.startsWith("6_")) {
                // 岗位
                jobTitleIdList.add(hrmResource[1]);
            }
        }

        if (!allIdList.isEmpty()) {
            //所有人
            RecordSet recordSet = new RecordSet();
            String sql = "select hrm.id from HrmResource hrm ";
            recordSet.executeQuery(sql);
            while (recordSet.next()) {
                receivers.add(recordSet.getString("id"));
            }
        } else {
            String hrmSql = "select hrm.id from HrmResource hrm ";
            String sqlWhere = " where 1=1 and hrm.id = 00 ";
            if (!hrmIdList.isEmpty()) {
                //人员
                String hrmIds = String.join(",", hrmIdList);
                sqlWhere += " or hrm.id in (" + hrmIds + ") ";
            }
            if (!subCompanyIdList.isEmpty()) {
                //分部
                String subCompanyId = String.join(",", subCompanyIdList);
                sqlWhere += " or hrm.subcompanyid1 in (" + subCompanyId + ") ";
            }
            if (!departmentIdList.isEmpty()) {
                //部门
                String departmentId = String.join(",", departmentIdList);
                sqlWhere += " or hrm.departmentid in (" + departmentId + ") ";
            }
            if (!jobTitleIdList.isEmpty()) {
                //岗位
                String jobTitleId = String.join(",", jobTitleIdList);
                sqlWhere += " or hrm.jobtitle in (" + jobTitleId + ")";
            }
            RecordSet rs = new RecordSet();
            rs.executeQuery(hrmSql + sqlWhere);
            while (rs.next()) {
                receivers.add(rs.getString("id"));
            }
            if (!roleIdList.isEmpty()) {
                //角色
                String roleId = String.join(",", roleIdList);
                String sql = "select hrmrole.resourceid from hrmrolemembers hrmrole where hrmrole.ROLEID in (" + roleId + ")";
                RecordSet rSet = new RecordSet();
                rSet.executeQuery(sql);
                while (rSet.next()) {
                    receivers.add(rSet.getString("resourceid"));
                }
            }
        }
        return receivers;
    }

    public void message2Resource(Map<String, Object> params) {
        // 接收人id 必填
        Set<String> userIdList = new HashSet<>();
        userIdList = (Set<String>) params.get("receiver");
        // 标题
        String title = ObjectUtils.toString(params.get("title"));
        // 内容
        String context = ObjectUtils.toString(params.get("context"));
        //创建人
        String creator = ObjectUtils.toString(params.get("creator"));
        String docId = ObjectUtils.toString(params.get("docId"));
        // 消息来源（见文档第四点补充 必填）
        MessageType messageType = MessageType.newInstance(ConstantInterface.MESSAGE_TO_RESOURCE);

        // PC端链接 http://oatest.zhenro.com
        String linkUrl = "/spa/document/index2New.jsp?openAttachment=1&id="+Integer.parseInt(docId)+"&newsDataType=sql&router=1&type=1#/main/document/detailNew";
        // 移动端链接     /spa/document/index2New.jsp?openAttachment=1&id=${id}&newsDataType=sql&router=1&type=1#/main/document/detailNew
        String linkMobileUrl = "移动端链接";
        try {
            MessageBean messageBean = Util_Message.createMessage(messageType, userIdList, title, context, linkUrl, linkMobileUrl);
            // 创建人id
            messageBean.setCreater(Integer.parseInt(creator));
            Util_Message.store(messageBean);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取流程表单
     *
     * @param formid
     * @param isbill
     * @return
     */
    private static String getTableName(int formid, int isbill) {
        String ret = "";
        if (isbill == 1) {
            String sql = "select tablename from workflow_bill where id = " + formid;
            RecordSet rs = new RecordSet();
            rs.execute(sql);
            if (rs.next()) {
                ret = rs.getString("tablename");
            }
        } else {
            ret = "workflow_form";
        }
        return ret;
    }
}

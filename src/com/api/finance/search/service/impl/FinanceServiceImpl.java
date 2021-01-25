package com.api.finance.search.service.impl;

import com.api.finance.entity.*;
import com.api.finance.search.service.FinanceService;
import com.api.finance.util.ConstantInterface;
import com.api.finance.util.TreeUtil;
import com.cloudstore.dev.api.bean.MessageBean;
import com.cloudstore.dev.api.bean.MessageType;
import com.cloudstore.dev.api.util.Util_Message;
import com.engine.core.impl.Service;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import weaver.conn.RecordSet;
import weaver.formmode.exttools.impexp.common.DateUtils;
import weaver.general.Util;
import weaver.hrm.User;

import java.io.IOException;
import java.util.*;

/**
 * 财务制度
 *
 * @author MZ
 * @version 0.1
 */
public class FinanceServiceImpl extends Service implements FinanceService {

    /**
     * 测试 （199, 202)
     * 本地 (24, 26)
     */
    public static final String tabSql = "select sec.id, sec.CATEGORYNAME, sec.PARENTID, sec.SECCATEGORYTYPE from docseccategory sec " +
            " where sec.PARENTID in (select id from docseccategory s where s.PARENTID = " + ConstantInterface.PARENT_ID +
            ") or sec.PARENTID = " + ConstantInterface.PARENT_ID;

    public String getTabSqlIds() {
        RecordSet rs = new RecordSet();
        rs.execute(tabSql);
        List<String> ids = new ArrayList<>();
        while (rs.next()) {
            ids.add(rs.getString("id"));
        }
        return String.join(",", ids);
    }

    @Override
    public String getTabSqlIdsMap(String key) {
        Map<String, String> categoryMap = new HashMap<>();
//        categoryMap.put("200", "20");
//        categoryMap.put("206", "21");
//        categoryMap.put("207", "22");
//        categoryMap.put("208", "23");
//        categoryMap.put("202", "24");
//        categoryMap.put("203", "25");
//        categoryMap.put("204", "26");
//        categoryMap.put("205", "27");
//        categoryMap.put("209", "28");

        categoryMap.put("203", "19");   //总部财务部
        categoryMap.put("202", "20");  //总部中心层面
        categoryMap.put("209", "21");  //财报规划组
        categoryMap.put("210", "22");  //利润管理组
        categoryMap.put("211", "23");  //资金管理组
        categoryMap.put("204", "24");  //总部税政部
        categoryMap.put("205", "25");  //总部资金部
        categoryMap.put("206", "26");  //总部资本部
        categoryMap.put("207", "27");  //总部组织综合组
        categoryMap.put("208", "28");  //区域财务制度

        // todo 生产环境需要重新写
        return categoryMap.get(key);
    }

    public String getTabSqlIdsMapValue(String key) {
        Map<String, String> categoryMap = new HashMap<>();
//        categoryMap.put("20","200");
//        categoryMap.put("21","206");
//        categoryMap.put("22","207");
//        categoryMap.put("23","208");
//        categoryMap.put("24","202");
//        categoryMap.put("25","203");
//        categoryMap.put("26","204");
//        categoryMap.put("27","205");
//        categoryMap.put("28","209");

        categoryMap.put("19","203");   //总部财务部
        categoryMap.put("20","202");  //总部中心层面
        categoryMap.put("21","209");  //财报规划组
        categoryMap.put("22","210");  //利润管理组
        categoryMap.put("23","211");  //资金管理组
        categoryMap.put("24","204");  //总部税政部
        categoryMap.put("25","205");  //总部资金部
        categoryMap.put("26","206");  //总部资本部
        categoryMap.put("27","207");  //总部组织综合组
        categoryMap.put("28","208");  //区域财务制度

        return categoryMap.get(key);
    }

    public String getTabSqlIdsList() {
        List<String> ids = new ArrayList<>();
        ids.add("19");
        ids.add("20");
        ids.add("21");
        ids.add("22");
        ids.add("23");
        ids.add("24");
        ids.add("25");
        ids.add("26");
        ids.add("27");
        ids.add("28");
        return String.join(",", ids);
    }

    @Override
    public Map<String, Object> getCategoryManager(int resourceId) {
        Map<String, Object> resultMap = new HashMap<>();
        boolean result = false;
        String sql = "select ml.id, mlgly, wdml from uf_mlgly ml where ml.mlgly = " + resourceId;
        RecordSet rs = new RecordSet();
        rs.execute(sql);
        List<Integer> ml = new ArrayList<>();
        while (rs.next()) {
            result = true;
            ml.add(rs.getInt("wdml"));
        }
        resultMap.put("isCategoryManager", result);
        resultMap.put("category", ml);
        return resultMap;
    }

    @Override
    public boolean getRoleByHrmId(int roleId, int resourceId) {
        boolean result = false;
        String sql = "select hrm.id, roleid, resourceid from hrmrolemembers hrm where hrm.ROLEID = " + roleId + " and hrm.resourceid = " + resourceId;
        RecordSet rs = new RecordSet();
        rs.execute(sql);
        while (rs.next()) {
            result = true;
        }
        return result;
    }

    @Override
    public int getRoleIdByRoleName(String name) {
        String sql = "select id from HrmRoles rol where rol.rolesmark = '" + name + "'";
        RecordSet rs = new RecordSet();
        rs.execute(sql);
        int roleId = 0;
        while (rs.next()) {
            roleId = rs.getInt("id");
        }
        return roleId;
    }

    @Override
    public Map<String, Object> getTadList(Map<String, Object> params, User user) {
        Map<String, Object> apidatas = new HashMap<>();
        RecordSet rs = new RecordSet();
        rs.execute(tabSql);
        List<TreeNode> tabList = new LinkedList<>();
        while (rs.next()) {
            TreeNode tab = new TreeNode();
            tab.setId(rs.getInt("id"));
            tab.setCategoryname(rs.getString("categoryname"));
            tab.setParentId(rs.getInt("parentid"));
            tab.setSeccategorytype(rs.getInt("seccategorytype"));
            tabList.add(tab);
        }
        List<TreeNode> treeNodes = TreeUtil.buildByRecursive(tabList, ConstantInterface.PARENT_ID);
        apidatas.put("datas", treeNodes);
        return apidatas;
    }

    @Override
    public Map<String, Object> getFinanceList(Map<String, Object> params, User user) {
        //发布公司
        int userSubCompany1 = user.getUserSubCompany1();
        boolean isAdmin = getRoleByHrmId(ConstantInterface.ROLE_ID, user.getUID());
        Map<String, Object> apidatas = new HashMap<>();
        RecordSet rs = new RecordSet();
        String sqlFields = "select db.id, db.zdbh,db.gwbt,db.ngr,db.fbsj,db.fblx,db.lcid,db.zdfjml,sum(log.OPERATETYPE = 0) ydl,detail.DOCSTATUS, log.OPERATETYPE,detail.id docid, detail.SECCATEGORY, hrm.LASTNAME,db.gs, com.SUBCOMPANYNAME ";
        String sqlFrom = " from uf_dbzx db\n" +
                "  left join docdetail detail on db.zwnr = detail.ID\n" +
                "  left join DocDetailLog log on detail.id = log.DOCID " +
                " inner join hrmresource hrm on hrm.id = db.ngr " +
                " left join hrmsubcompany com on com.id = db.gs ";
        String sqlWhere = " where 1=1 ";
        String sqlGroup = " group by db.id ";
        String orderBy = " order by db.id desc ";
        //制度标题
        String gwbt = Util.null2String(params.get("wdbt"));
        if (StringUtils.isNotBlank(gwbt)) {
            sqlWhere += " and db.gwbt like '%" + gwbt + "%' ";
        }
        //制度编号
        String zdbh = Util.null2String(params.get("zdbh"));
        if (StringUtils.isNotBlank(zdbh)) {
            sqlWhere += " and db.zdbh like '%" + zdbh + "%' ";
        }
        //发布时间
        String fbsj = Util.null2String(params.get("fbsj"));
        if (StringUtils.isNotBlank(fbsj)) {
            sqlWhere += " and db.fbsj = '" + fbsj + "' ";
        }
        //发布人
        String lastName = Util.null2String(params.get("lastName"));
        if (StringUtils.isNotBlank(lastName)) {
            sqlWhere += " and hrm.LASTNAME like '%" + lastName + "%' ";
        }
        //公司id
        String gs = Util.null2String(params.get("gs"));
        if (StringUtils.isNotBlank(gs)) {
            sqlWhere += " and db.gs = '" + gs + "' ";
        }
        //公司名称
        String companyName = Util.null2String(params.get("companyName"));
        if (StringUtils.isNotBlank(companyName)) {
            sqlWhere += " and com.SUBCOMPANYNAME like '%" + companyName + "%' ";
        }
        //状态 1=(1,2)已生效    2=(3)审批中    3=(0,4)草稿     4=(7,8)废止
        String docstatus = Util.null2String(params.get("docstatus"));
        if (StringUtils.isNotBlank(docstatus)) {
            if (StringUtils.equals("1", docstatus)) {
                docstatus = "1,2";
            } else if (StringUtils.equals("2", docstatus)) {
                docstatus = "3";
            } else if (StringUtils.equals("3", docstatus)) {
                docstatus = "0,4";
            } else {
                docstatus = "7,8";
            }
//            sqlWhere += " and detail.DOCSTATUS =  '" + docstatus + "' ";
            sqlWhere += " and detail.DOCSTATUS in (" + docstatus + ") ";
        }
        //文档目录id
        String ids = getTabSqlIdsList(); //getTabSqlIds();
        String seccategory = Util.null2String(params.get("seccategory"));
        if (StringUtils.isNotBlank(seccategory)) {
            //todo 0代表全部
            if (StringUtils.equals("0", seccategory)) {
//                sqlWhere += " and detail.SECCATEGORY in (" + ids + ") ";
                sqlWhere += " and db.zdfjml in (" + ids + ") ";
            } else if(StringUtils.equals("19", seccategory)) {
                // todo 19 财务部
                sqlWhere += " and db.zdfjml in (21,22,23) ";
            } else {
//                sqlWhere += " and detail.SECCATEGORY =  '" + seccategory + "' ";
                sqlWhere += " and db.zdfjml =  '" + getTabSqlIdsMap(seccategory) + "' ";
            }
        }
        String sql = sqlFields + sqlFrom + sqlWhere + sqlGroup + orderBy;
        rs.execute(sql);
        List<Finance> financeList = new LinkedList<>();
        Map<String, Object> categoryManager = getCategoryManager(user.getUID());
        boolean isCategoryManaer = (boolean) categoryManager.get("isCategoryManager");
        List<Integer> ml = new ArrayList<>();
        if (isCategoryManaer) {
            ml = (List<Integer>) categoryManager.get("category");
        }
        while (rs.next()) {
            Finance finance = new Finance();
            finance.setId(rs.getInt("id"));
            finance.setDocid(rs.getInt("docid"));
            finance.setZdbh(rs.getString("zdbh"));
            finance.setWdbt(rs.getString("gwbt"));
            finance.setNgr(rs.getInt("ngr"));
            finance.setFbsj(rs.getString("fbsj"));
            finance.setFblx(rs.getInt("fblx"));
            finance.setDocstatus(rs.getInt("docstatus"));
            finance.setYdl(rs.getInt("ydl"));
            finance.setLastName(rs.getString("LASTNAME"));
            finance.setGs(rs.getInt("gs"));
            finance.setCompanyName(rs.getString("SUBCOMPANYNAME"));
            finance.setLcid(rs.getInt("lcid"));
            //是目录管理中且发布公司一致
            int companyID = rs.getInt("gs");
//            if (ml.contains(rs.getInt("SECCATEGORY")) && userSubCompany1 == companyID) {
            if (ml.contains(getTabSqlIdsMapValue(rs.getString("zdfjml"))) && userSubCompany1 == companyID) {
                finance.setAbolish(true);
            } else {
                finance.setAbolish(false);
            }
            //状态不是已生效 不是目录管理员，也不是创建人
            if(!isCategoryManaer && rs.getInt("docstatus") != 1 && rs.getInt("docstatus") != 2 && rs.getInt("ngr") != user.getUID()&&!isAdmin){
                continue;
            }
            if (rs.getInt("fblx") == 0 && (rs.getInt("docstatus") == 1 || rs.getInt("docstatus") == 2)) {
                Finance financeByLcid = getFinanceByLcid(rs.getInt("lcid"), rs.getInt("zdfjml"), ml, userSubCompany1);
                if(!isCategoryManaer && financeByLcid.getDocstatus() != 1 && financeByLcid.getDocstatus() != 2 && financeByLcid.getNgr() != user.getUID() && !isAdmin){
                    continue;
                }
                financeList.add(financeByLcid);
            } else {
                financeList.add(finance);
            }
        }
        apidatas.put("datas", financeList);
        apidatas.put("totalCount", financeList.size());
        return apidatas;
    }

    private Finance getFinanceByLcid(Integer lcid, Integer zdfjml, List<Integer> ml, int userSubCompany1) {
        Finance finance = new Finance();
        String ids = getTabSqlIds();
        String sqlFields = "select db.id, db.zdbh,db.gwbt,db.ngr,db.fbsj,db.fblx,db.lcid,sum(log.OPERATETYPE = 0) ydl,detail.DOCSTATUS, log.OPERATETYPE,detail.id docid, detail.SECCATEGORY, hrm.LASTNAME,db.gs, com.SUBCOMPANYNAME ";
        String sqlFrom = " from uf_dbzx db " +
                "  left join docdetail detail on db.zwnr = detail.ID " +
                "  left join DocDetailLog log on detail.id = log.DOCID " +
                " inner join hrmresource hrm on hrm.id = db.ngr " +
                " left join hrmsubcompany com on com.id = db.gs ";
        String sqlWhere = " where 1=1 and db.lcid = " + lcid + " and detail.SECCATEGORY = " + getTabSqlIdsMapValue(zdfjml.toString());
        String sqlGroup = " group by db.id ";
        String sql = sqlFields + sqlFrom + sqlWhere + sqlGroup;
        RecordSet rs = new RecordSet();
        rs.execute(sql);

        if (rs.next()) {
            finance.setId(rs.getInt("id"));
            finance.setDocid(rs.getInt("docid"));
            finance.setZdbh(rs.getString("zdbh"));
            finance.setWdbt(rs.getString("gwbt"));
            finance.setNgr(rs.getInt("ngr"));
            finance.setFbsj(rs.getString("fbsj"));
            finance.setFblx(rs.getInt("fblx"));
            finance.setDocstatus(rs.getInt("docstatus"));
            finance.setYdl(rs.getInt("ydl"));
            finance.setLastName(rs.getString("LASTNAME"));
            finance.setGs(rs.getInt("gs"));
            finance.setCompanyName(rs.getString("SUBCOMPANYNAME"));
            finance.setLcid(rs.getInt("lcid"));
            //是目录管理中且发布公司一致
            int companyID = rs.getInt("gs");
            if (ml.contains(rs.getInt("SECCATEGORY")) && userSubCompany1 == companyID) {
                finance.setAbolish(true);
            } else {
                finance.setAbolish(false);
            }
        }
        return finance;
    }

    @Override
    public Map<String, Object> getWorkflowAddress(Map<String, Object> params, User user) {
        Map<String, Object> apidatas = new HashMap<>();
        int uid = user.getUID();
        //根据用户id查询用户信息, 主次账号  30265  23427
        String supSubComSql = "select com.SUBCOMPANYNAME,com.SUPSUBCOMID from hrmsubcompany com where com.id in " +
                "(select hrm.SUBCOMPANYID1 from hrmresource hrm where hrm.id = " + uid + " or hrm.BELONGTO= " + uid + ")";
        //String subCompanySql = "select hrm.SUBCOMPANYID1 from hrmresource hrm where hrm.id = "+ uid +" or hrm.BELONGTO= " + uid;
        RecordSet rs = new RecordSet();
        rs.execute(supSubComSql);
        List<String> subCompanyName = new ArrayList<>();
        while (rs.next()) {
            subCompanyName.add(rs.getString("SUBCOMPANYNAME"));
            getCategory(rs.getInt("SUPSUBCOMID"), subCompanyName);
        }
        apidatas.put("datas", handleAddress(subCompanyName));
        return apidatas;
    }

    public static List<Map<String, String>> handleAddress(List<String> subCompanyName) {
        List<Map<String, String>> address = new ArrayList<>();
        Map<String, String> workFlowAdress = addressToMap();
        for (String key : workFlowAdress.keySet()) {
            if (subCompanyName.contains(key)) {
                Map<String, String> map = new HashMap<>();
                map.put("supCompany", key);
                map.put("workFlowId", workFlowAdress.get(key));
                address.add(map);
            }
        }
        return address;
    }

    /**
     * 流程地址id
     *
     * @return
     */
    public static Map<String, String> addressToMap() {
        Map<String, String> map = new HashMap<>();
        //http://oatest.zhenro.com/spa/workflow/static4form/index.html?_rdm=1600226265976#/main/workflow/req?iscreate=1&workflowid=
        map.put(ConstantInterface.REAL_ESTATE, ConstantInterface.ESTATE_WORKFLOW);
        map.put(ConstantInterface.GROUP_HEADQUARTERS, ConstantInterface.GROUP_WORKFLOW);
        map.put(ConstantInterface.SERVICE_HEADQUARTERS, ConstantInterface.SERVICE_WORKFLOW);
        map.put(ConstantInterface.SUB_COMPANY, ConstantInterface.SUB_COM_WORKFLOW);
        map.put(ConstantInterface.PROPERTY_AREA, ConstantInterface.AREA_WORKFLOW);
        return map;
    }

    /**
     * 查询当前登录人所在分部，
     *
     * @param supCategory
     * @param subCompanyName
     */
    public static void getCategory(int supCategory, List<String> subCompanyName) {
        String sql = "select com.SUBCOMPANYNAME, com.SUPSUBCOMID from hrmsubcompany com where com.id = " + supCategory;
        if (supCategory == 0) {
            return;
        }
        RecordSet rs = new RecordSet();
        rs.execute(sql);
        int category = 0;
        while (rs.next()) {
            subCompanyName.add(rs.getString("SUBCOMPANYNAME"));
            category = rs.getInt("SUPSUBCOMID");
        }
        getCategory(category, subCompanyName);
    }

    @Override
    public boolean abolish(Map<String, Object> params, User user) {
        Object docid = params.get("docid");
        String sql = "update docdetail detail set detail.DOCSTATUS = 8 where detail.ID = " + docid;
        RecordSet rs = new RecordSet();
        return rs.executeUpdate(sql);
    }

    @Override
    public Map<String, Object> getEconomics(Map<String, Object> params, User user) {
        Map<String, Object> apidatas = new HashMap<>();
        List<Economics> economicsList = new ArrayList<>();
        Object docid = params.get("docid");
        String sql = "select nr, zt, docid from uf_cjxy where docid = " + docid;
        RecordSet rs = new RecordSet();
        rs.execute(sql);
        while (rs.next()) {
            Economics economics = new Economics();
            economics.setNr(rs.getString("nr"));
            economics.setZt(rs.getInt("zt"));
            economics.setDocid(rs.getInt("docid"));
            economicsList.add(economics);
        }
        apidatas.put("datas", economicsList);
        return apidatas;
    }

    @Override
    public boolean saveEconomics(Map<String, Object> params, User user) {
        Object docid = params.get("docid");
        Object nr = params.get("nr");
        Object zt = params.get("zt");
        String getSql = "select nr, zt, docid from uf_cjxy where docid = " + docid;
        RecordSet recordSet = new RecordSet();
        recordSet.execute(getSql);
        String sql = "";
        if (recordSet.next()) {
            sql = "update uf_cjxy set zt = " + zt + ", nr = '" + nr + "' where docid = " + docid;
        } else {
            sql = "insert into uf_cjxy (nr, zt, docid) values ('" + nr + "'," + zt + "," + docid + ")";
        }
        RecordSet rs = new RecordSet();
        return rs.execute(sql);
    }

    @Override
    public boolean updateEconomics(Map<String, Object> params, User user) {
        Object docid = params.get("docid");
        Object nr = params.get("nr");
        Object zt = params.get("zt");
        String sql = "update uf_cjxy set zt = " + zt + ", nr = '" + nr + "' where docid = " + docid;
        RecordSet rs = new RecordSet();
        return rs.executeUpdate(sql);
    }

    @Override
    public Map<String, Object> getInterpretation(Map<String, Object> params, User user) {
        Map<String, Object> apidatas = new HashMap<>();
        List<Economics> economicsList = new ArrayList<>();
        Object docid = params.get("docid");
        String sql = "select nr, zt, docid from uf_zdjd where docid = " + docid;
        RecordSet rs = new RecordSet();
        rs.execute(sql);
        while (rs.next()) {
            Economics economics = new Economics();
            economics.setNr(rs.getString("nr"));
            economics.setZt(rs.getInt("zt"));
            economics.setDocid(rs.getInt("docid"));
            economicsList.add(economics);
        }
        apidatas.put("datas", economicsList);
        return apidatas;
    }

    @Override
    public boolean saveInterpretation(Map<String, Object> params, User user) {
        Object docid = params.get("docid");
        Object nr = params.get("nr");
        Object zt = params.get("zt");
        String getSql = "select nr, zt, docid from uf_zdjd where docid = " + docid;
        RecordSet recordSet = new RecordSet();
        recordSet.execute(getSql);
        String sql = "";
        if (recordSet.next()) {
            sql = "update uf_zdjd set zt = " + zt + ", nr = '" + nr + "' where docid = " + docid;
        } else {
            sql = "insert into uf_zdjd (nr, zt, docid) values ('" + nr + "'," + zt + "," + docid + ")";
        }
        RecordSet rs = new RecordSet();
        return rs.execute(sql);
    }

    @Override
    public boolean updateInterpretation(Map<String, Object> params, User user) {
        Object docid = params.get("docid");
        Object nr = params.get("nr");
        Object zt = params.get("zt");
        String sql = "update uf_zdjd set zt = " + zt + ", nr = '" + nr + "' where docid = " + docid;
        RecordSet rs = new RecordSet();
        return rs.executeUpdate(sql);
    }

    @Override
    public Map<String, Object> getReviewList(Map<String, Object> params, User user) {
        Map<String, Object> apidatas = new HashMap<>();
        List<Review> reviewList = new ArrayList<>();
        String sqlwhere = " where 1=1 and pl.docid = detail.id and hrm.id = pl.plr ";
        String orderBy = " order by pl.id desc ";
        boolean isAdmin = getRoleByHrmId(ConstantInterface.ROLE_ID, user.getUID());
        //制度标题
        String zdbt = Util.null2String(params.get("zdbt"));
        if (StringUtils.isNotBlank(zdbt)) {
            sqlwhere += " and pl.zdbt like '%" + zdbt + "%' ";
        }
        //回复状态
        String hfzt = Util.null2String(params.get("hfzt"));
        if (StringUtils.isNotBlank(hfzt)) {
            sqlwhere += " and pl.hfzt = '" + hfzt + "' ";
        }
        //状态
        String zt = Util.null2String(params.get("zt"));
        if (StringUtils.isNotBlank(zt)) {
            sqlwhere += " and pl.zt = '" + zt + "' ";
        }
        //评论时间
        String plsj = Util.null2String(params.get("plsj"));
        if (StringUtils.isNotBlank(plsj)) {
            sqlwhere += " and pl.plsj = '" + plsj + "' ";
        }
        //评论人
        String lastName = Util.null2String(params.get("lastName"));
        if (StringUtils.isNotBlank(lastName)) {
            if (!isAdmin) {
                sqlwhere += " and pl.nm = 1 ";
            }
            sqlwhere += " and hrm.LASTNAME like '%" + lastName + "%' ";
        }
        //文档id
        String docid = Util.null2String(params.get("docid"));
        if (StringUtils.isNotBlank(docid)) {
            sqlwhere += " and pl.docid = '" + docid + "' ";
        }
        //如果是文档目录管理员

        Map<String, Object> categoryManager = getCategoryManager(user.getUID());
        boolean isCategoryManaer = (boolean) categoryManager.get("isCategoryManager");
        List<Integer> ml;
        //如果不是isAdmin且是目录管理员 todo 目录管理员可看自己目录下的，超级管理员看全部，其它人无权看
        String secCategory = "";
        if (!isAdmin && isCategoryManaer) {
            ml = (List<Integer>) categoryManager.get("category");
            for (int i = 0; i < ml.size(); i++) {
                if (i != ml.size() - 1) {
                    secCategory += ml.get(i) + ",";
                } else {
                    secCategory += ml.get(i) + "";
                }
            }
            if (StringUtils.isNotBlank(secCategory)) {
                sqlwhere += " and detail.SECCATEGORY in (" + secCategory + ") ";
            }
        }
        apidatas.put("categoryManager", categoryManager);
        apidatas.put("isAdmin", isAdmin);

        String sqlFields = "select pl.id, pl.zt, pl.hfzt, pl.plnr, pl.plr, pl.plsj, pl.nm, pl.docid, hrm.LASTNAME, detail.DOCSUBJECT as zdbt ";
        String sqlFrom = " from uf_zdpl pl, DocDetail detail, hrmresource hrm ";
        String sql = sqlFields + sqlFrom + sqlwhere + orderBy;
        RecordSet rs = new RecordSet();
        rs.execute(sql);
        while (rs.next()) {
            Review review = new Review();
            review.setId(rs.getInt("id"));
            review.setZdbt(rs.getString("zdbt"));
            review.setZt(rs.getInt("zt"));
            review.setHfzt(rs.getInt("hfzt"));
            review.setPlnr(rs.getString("plnr"));
            review.setPlr(rs.getInt("plr"));
            review.setPlsj(rs.getString("plsj"));
            review.setNm(rs.getInt("nm"));
            review.setDocid(rs.getInt("docid"));
            review.setLastName(rs.getString("LASTNAME"));
            reviewList.add(review);
        }
        //全部（总数）、1作废（总数）、0正常（总数）三个模块
        apidatas.put("normal", getCount("0", secCategory));
        apidatas.put("canceled", getCount("1", secCategory));
        apidatas.put("all", getCount("all", secCategory));
        apidatas.put("datas", reviewList);
        return apidatas;
    }

    public int getCount(String type, String secCategory) {
        String countSqlWhere = "";
        String countSql = "";
        if (StringUtils.isNotBlank(secCategory)) {
            countSqlWhere += " and detail.SECCATEGORY in (" + secCategory + ") ";
        }
        if ("all".equals(type)) {
            countSql = "select count(*)as num from uf_zdpl pl, DocDetail detail,hrmresource hrm where pl.docid = detail.id and hrm.id = pl.plr " + countSqlWhere;
        } else {
            countSql = "select count(*)as num from uf_zdpl pl, DocDetail detail,hrmresource hrm where pl.docid = detail.id and hrm.id = pl.plr " + " and pl.zt = " + type + countSqlWhere;
        }
        RecordSet rs = new RecordSet();
        rs.execute(countSql);
        int num = 0;
        while (rs.next()) {
            num = rs.getInt("num");
        }
        return num;
    }

    @Override
    public boolean saveReview(Map<String, Object> params, User user, Review review) {
        Integer docid = review.getDocid();
        Integer hfzt = review.getHfzt();
        String plnr = review.getPlnr();
        Integer plr = user.getUID();
        String plsj = DateUtils.getCurrentDateTime();
        Integer nm = review.getNm();
        Integer zt = review.getZt();
        String zdbt = review.getZdbt();

//        Object docid = params.get("docid");
//        Object hfzt = params.get("hfzt");
//        Object zdbt = params.get("zdbt");
//        Object plnr = params.get("plnr");
//        Object plr = params.get("plr");
//        Object plsj = params.get("plsj");
//        Object zt = params.get("zt");
//        Object nm = params.get("nm");
        String insertSql = "insert into uf_zdpl (hfzt, zdbt, plnr, plr, plsj, zt, nm, docid) values (?,?,?,?,?,?,?,?)";
        String sql = "insert into uf_zdpl (hfzt, zdbt, plnr, plr, plsj, zt, nm, docid) values ("
                + hfzt + ",'" + zdbt + "','" + plnr + "'," + plr + ",'" + plsj + "'," + zt + "," + nm + "," + docid + ")";
        RecordSet rs = new RecordSet();
        boolean result = rs.execute(sql);
        if (result) {
            String detailSql = "select detail.SECCATEGORY,detail.DOCSUBJECT from docdetail detail where detail.id =" + docid;
            RecordSet recordDetail = new RecordSet();
            recordDetail.execute(detailSql);
            int secCategory = 0;
            String title = "";
            while (recordDetail.next()) {
                secCategory = recordDetail.getInt("SECCATEGORY");
                title = recordDetail.getString("DOCSUBJECT");
            }
            // 发给对应的目录管理员
            String mlSql = "select ml.mlgly from uf_mlgly ml where ml.wdml = " + secCategory;
            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("title", title);
            RecordSet recordSet = new RecordSet();
            recordSet.execute(mlSql);
            Set<String> receiverSet = new HashSet<>();
            while (recordSet.next()) {
                receiverSet.add(recordSet.getString("mlgly"));
            }
            messageMap.put("receiver", receiverSet);
            StringBuilder sb = new StringBuilder();
            sb.append("评论内容：");
            sb.append(plnr);
            sb.append("<br/>");
            sb.append("评论时间：");
            sb.append(plsj);
            messageMap.put("context", sb);
            this.message2CategoryManager(messageMap, user);
        }
        return result;
    }

    @Override
    public Map<String, Object> getReviewListByDocid(Map<String, Object> params, User user) {
        Map<String, Object> apidatas = new HashMap<>();
        List<Review> reviewList = new ArrayList<>();
        String sqlwhere = " where pl.zt = 0 and pl.plr = hrm.id ";
        String sqlOrder = "order by pl.id desc ";
        int pageNo = 1;
        int pageSize = 5;
        String pageNo1 = Util.null2String(params.get("pageNo"));
        if (StringUtils.isNotBlank(pageNo1)) {
            pageNo = Integer.parseInt(pageNo1);
        }
        String pageSize1 = Util.null2String(params.get("pageSize"));
        if (StringUtils.isNotBlank(pageSize1)) {
            pageSize = Integer.parseInt(pageSize1);
        }
        int rows = (pageNo - 1) * pageSize;
        String sqlLimit = "limit " + rows + "," + pageSize;
        String docid = Util.null2String(params.get("docid"));
        if (StringUtils.isNotBlank(docid)) {
            sqlwhere += " and pl.docid = '" + docid + "' ";
        }
        String baseSql = "select pl.id, pl.zdbt, pl.zt, pl.hfzt, pl.plnr, pl.plr, pl.plsj, pl.nm, pl.docid, hrm.LASTNAME, hrm.messagerurl, hrm.sex from uf_zdpl pl, hrmresource hrm ";
        String sql = baseSql + sqlwhere + sqlOrder + sqlLimit;
        RecordSet rs = new RecordSet();
        rs.execute(sql);
        while (rs.next()) {
            Review review = new Review();
            review.setId(rs.getInt("id"));
            review.setZdbt(rs.getString("zdbt"));
            review.setZt(rs.getInt("zt"));
            review.setHfzt(rs.getInt("hfzt"));
            review.setPlnr(rs.getString("plnr"));
            review.setPlr(rs.getInt("plr"));
            review.setPlsj(rs.getString("plsj"));
            review.setNm(rs.getInt("nm"));
            review.setDocid(rs.getInt("docid"));
            review.setCommentReview(getCommentReviewByReviewId(rs.getInt("id")));
            review.setLastName(rs.getString("LASTNAME"));
            //todo 默认头像
            review.setMessagerurl(rs.getString("messagerurl"));
            review.setSex(rs.getInt("sex"));
            reviewList.add(review);
        }
        //全部（总数）、1作废（总数）、0正常（总数）三个模块
        String countSql = " select count(pl.id) count from uf_zdpl pl, hrmresource hrm " + sqlwhere;
        RecordSet recordSet = new RecordSet();
        recordSet.execute(countSql);
        int count = 0;
        while (recordSet.next()) {
            count = recordSet.getInt("count");
        }
        apidatas.put("count", count);
        apidatas.put("datas", reviewList);
        return apidatas;
    }

    @Override
    public boolean updateReviewStatusById(Map<String, Object> params, User user) {
        Object id = params.get("id");
        Object zt = params.get("zt");
        String sql = "update uf_zdpl set zt = " + zt + " where id = " + id;
        RecordSet rs = new RecordSet();
        return rs.executeUpdate(sql);
    }

    @Override
    public boolean updateReviewReplyStatusById(Object id, Integer hfzt) {
        String sql = "update uf_zdpl set hfzt = " + hfzt + " where id = " + id;
        RecordSet rs = new RecordSet();
        return rs.executeUpdate(sql);
    }

    @Override
    public boolean saveCommentReview(Map<String, Object> params, User user) {
        Object plid = params.get("plid");
        Object hfnr = params.get("hfnr");
        Object hfsj = DateUtils.getCurrentDateTime();
        Object hfr = user.getUID();
        String searchSql = "select * from uf_zdplhf hf where hf.plid = " + plid;
        RecordSet recordSet = new RecordSet();
        recordSet.execute(searchSql);
        String sql = "";
        RecordSet rs = new RecordSet();
        if (recordSet.next()) {
            sql = "update uf_zdplhf set plid = " + plid + ", hfnr = '" + hfnr + "', hfsj = '" + hfsj + "', hfr = " + hfr + " where plid = " + plid;
        } else {
            //sql = "insert into uf_zdplhf (plid, hfnr, hfsj, hfr) values (？,？,？,？)";
            sql = "insert into uf_zdplhf (plid, hfnr, hfsj, hfr) values (" + plid + ",'" + hfnr + "','" + hfsj + "'," + hfr + ")";
            //return rs.executeUpdate(sql, plid, hfnr, hfsj, hfr);
        }
        this.updateReviewReplyStatusById(plid, 0);
        boolean result = rs.executeUpdate(sql);
//        if (result) {
//            //todo 评论消息通知
//            //内容显示 制度标题，评论人，评论内容，评论时间，评论回复内容，回复时间，回复人
//            Map<String, Object> messageMap = new HashMap<>();
//            messageMap.put("hfr", hfr);
//            messageMap.put("hfsj", hfsj);
//            messageMap.put("hfnr", hfnr);
//            messageMap.put("lastName", user.getLastname());
//            String plSql = "select pl.id, pl.plnr, pl.plr, pl.docid, pl.plsj, detail.DOCSUBJECT zdbt from uf_zdpl pl, docdetail detail where pl.docid = detail.id and pl.id = " + plid;
//            RecordSet recordSetTwo = new RecordSet();
//            recordSetTwo.execute(plSql);
//            while (recordSetTwo.next()) {
//                messageMap.put("plr", recordSetTwo.getInt("plr"));
//                messageMap.put("plnr", recordSetTwo.getString("plnr"));
//                messageMap.put("plsj", recordSetTwo.getString("plsj"));
//                messageMap.put("zdbt", recordSetTwo.getString("zdbt"));
//                messageMap.put("docid", recordSetTwo.getString("docid"));
//            }
//            this.message(messageMap, user);
//        }
        return result;
    }

    @Override
    public CommentReview getCommentReviewByReviewId(Object plid) {
        String sql = "select hf.id, hf.plid, hf.hfnr, hf.hfsj, hf.hfr,hrm.lastname from uf_zdplhf hf, HrmResource hrm where hf.hfr = hrm.id and hf.plid = " + plid;
        RecordSet rs = new RecordSet();
        rs.execute(sql);
        CommentReview commentReview = new CommentReview();
        while (rs.next()) {
            commentReview.setId(rs.getInt("id"));
            commentReview.setPlid(rs.getInt("plid"));
            commentReview.setHfnr(rs.getString("hfnr"));
            commentReview.setHfsj(rs.getString("hfsj"));
            commentReview.setHfr(rs.getInt("hfr"));
            commentReview.setLastName(rs.getString("lastname"));
        }
        return commentReview;
    }

    @Override
    public void message(Map<String, Object> params, User user) {
        String plr = ObjectUtils.toString(params.get("plr"));
        String plnr = ObjectUtils.toString(params.get("plnr"));
        String plsj = ObjectUtils.toString(params.get("plsj"));
        String zdbt = ObjectUtils.toString(params.get("zdbt"));
        String hfr = ObjectUtils.toString(params.get("hfr"));
        String hfsj = ObjectUtils.toString(params.get("hfsj"));
        String hfnr = ObjectUtils.toString(params.get("hfnr"));
        String lastName = ObjectUtils.toString(params.get("lastName"));
        String docid = ObjectUtils.toString(params.get("docid"));

        // 消息来源（见文档第四点补充 必填）
        MessageType messageType = MessageType.newInstance(ConstantInterface.MESSAGE_TYPE);
        // 接收人id 必填
        Set<String> userIdList = new HashSet<>();
        userIdList.add(ObjectUtils.toString(params.get("plr")));
//        userIdList.add("52");
        userIdList.add(plr);
        // 标题
        String title = zdbt;
        // 内容
//        String context = "附件名称创建人：系统管理员<br/>开始时间：2020-08-04 11:29<br/>结束时间：2020-08-28 11:29";
        StringBuilder context = new StringBuilder();
        context.append("评论内容：");
        context.append(plnr);
        context.append("<br/>");
        context.append("评论时间：");
        context.append(plsj);
        context.append("<br/>");
        context.append("回复内容：");
        context.append(hfnr);
        context.append("<br/>");
        context.append("回复时间：");
        context.append(hfsj);
        context.append("<br/>");
        context.append("回复人：");
        context.append(lastName);
        //String linkUrl = "http://oatest.zhenro.com/wui/index.html#/main/cs/app/961086b07e4346a49095a044a3a9b301_ofsFinancialList?menuIds=-40,-41&_key=i5csf1"; // PC端链接
        String linkUrl = "/spa/document/index2New.jsp?openAttachment=1&id=" + Integer.parseInt(docid) + "&newsDataType=sql&router=1&type=1#/main/document/detailNew"; // PC端链接
        String linkMobileUrl = "移动端链接"; // 移动端链接
        try {
            MessageBean messageBean = Util_Message.createMessage(messageType, userIdList, title, context.toString(), linkUrl, linkMobileUrl);
            // 创建人id
            messageBean.setCreater(Integer.parseInt(hfr));
            //message.setBizState("0");// 需要修改消息为已处理等状态时传入,表示消息最初状态为待处理
            //messageBean.setTargetId("121|22"); //消息来源code +“|”+业务id需要修改消息为已处理等状态时传入
            Util_Message.store(messageBean);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void message2CategoryManager(Map<String, Object> params, User user) {
        // 接收人id 必填
        Set<String> userIdList = new HashSet<>();
        userIdList = (Set<String>) params.get("receiver");
        // 标题
        String title = ObjectUtils.toString(params.get("title"));
        String context = ObjectUtils.toString(params.get("context"));
        // 消息来源（见文档第四点补充 必填）
        MessageType messageType = MessageType.newInstance(ConstantInterface.MESSAGE_TO_CATEGORY_TYPE);
        // 内容  http://oatest.zhenro.com
        String linkUrl = "/wui/index.html?templateId=2&from=static#/main/cs/app/ac2ccca21e9a481d8271129dbb6243bd_ofsCommentList?menuIds=-40,-42&_key=rcfxsg"; // PC端链接
        //
        String linkMobileUrl = "移动端链接"; // 移动端链接
        try {
            MessageBean messageBean = Util_Message.createMessage(messageType, userIdList, title, context, linkUrl, linkMobileUrl);
            // 创建人id
            messageBean.setCreater(user.getUID());
            Util_Message.store(messageBean);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

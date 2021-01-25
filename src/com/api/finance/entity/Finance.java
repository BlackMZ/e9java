package com.api.finance.entity;

/**
 * 财务信息
 */
public class Finance {

    /**
     * id
     */
    private Integer id;

    /**
     * 文档id
     */
    private Integer docid;

    /**
     * 制度编号
     */
    private String zdbh;

    /**
     * 文档标题
     */
    private String wdbt;


    /**
     * 拟稿人
     */
    private Integer ngr;

    /**
     * 发布时间
     */
    private String fbsj;

    /**
     * 发布类型 0 通知公告  1非公告
     */
    private Integer fblx;
    /**
     * 状态
     */
    private Integer docstatus;

    /**
     * 阅读量
     */
    private Integer ydl;

    /**
     * 废止按钮是否显示
     */
    private Boolean abolish;

    /**
     * 发布时间
     */
    private String lastName;


    /**
     * 发布公司id
     */
    private Integer gs;

    /**
     * 流程id
     */
    private Integer lcid;

    /**
     * 发布公司名称
     */
    private String companyName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDocid() {
        return docid;
    }

    public void setDocid(Integer docid) {
        this.docid = docid;
    }

    public String getZdbh() {
        return zdbh;
    }

    public void setZdbh(String zdbh) {
        this.zdbh = zdbh;
    }

    public String getWdbt() {
        return wdbt;
    }

    public void setWdbt(String wdbt) {
        this.wdbt = wdbt;
    }

    public Integer getNgr() {
        return ngr;
    }

    public void setNgr(Integer ngr) {
        this.ngr = ngr;
    }

    public String getFbsj() {
        return fbsj;
    }

    public void setFbsj(String fbsj) {
        this.fbsj = fbsj;
    }

    public Integer getFblx() {
        return fblx;
    }

    public void setFblx(Integer fblx) {
        this.fblx = fblx;
    }

    public Integer getDocstatus() {
        return docstatus;
    }

    public void setDocstatus(Integer docstatus) {
        this.docstatus = docstatus;
    }

    public Integer getYdl() {
        return ydl;
    }

    public void setYdl(Integer ydl) {
        this.ydl = ydl;
    }

    public Boolean getAbolish() {
        return abolish;
    }

    public void setAbolish(Boolean abolish) {
        this.abolish = abolish;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getGs() {
        return gs;
    }

    public void setGs(Integer gs) {
        this.gs = gs;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getLcid() {
        return lcid;
    }

    public void setLcid(Integer lcid) {
        this.lcid = lcid;
    }
}

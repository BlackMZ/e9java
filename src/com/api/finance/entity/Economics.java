package com.api.finance.entity;

/**
 * 财经学院
 */
public class Economics {

    /** 内容 */
    private String nr;

    /** 状态 */
    private Integer zt;

    /** 文档id */
    private Integer docid;

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    public Integer getZt() {
        return zt;
    }

    public void setZt(Integer zt) {
        this.zt = zt;
    }

    public Integer getDocid() {
        return docid;
    }

    public void setDocid(Integer docid) {
        this.docid = docid;
    }
}

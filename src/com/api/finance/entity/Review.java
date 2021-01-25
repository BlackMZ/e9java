package com.api.finance.entity;

/**
 * @author Arlo
 * @version 1.0.0
 * @Description 制度评论
 * @ClassName Review.java
 * @createTime 2020年12月25日 10:27:00
 */
public class Review {

    /** id */
    private Integer id;
    /** 制度标题 */
    private String zdbt;
    /** 状态 0正常， 1作废*/
    private Integer zt;
    /** 回复状态 0 已回复 1未回复*/
    private Integer hfzt;
    /** 评论内容 */
    private String plnr;
    /** 评论人 */
    private Integer plr;
    /** 评论时间 */
    private String plsj;
    /** 是否匿名0是  1否 */
    private Integer nm;
    /** 文档id */
    private Integer docid;
    /** 用户名 */
    private String lastName;
    /** 图片地址 */
    private String messagerurl;

    /** 性别 */
    private Integer sex;
    /**
     * 评论回复
     */
    private CommentReview commentReview;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getZdbt() {
        return zdbt;
    }

    public void setZdbt(String zdbt) {
        this.zdbt = zdbt;
    }

    public Integer getZt() {
        return zt;
    }

    public void setZt(Integer zt) {
        this.zt = zt;
    }

    public Integer getHfzt() {
        return hfzt;
    }

    public void setHfzt(Integer hfzt) {
        this.hfzt = hfzt;
    }

    public String getPlnr() {
        return plnr;
    }

    public void setPlnr(String plnr) {
        this.plnr = plnr;
    }

    public Integer getPlr() {
        return plr;
    }

    public void setPlr(Integer plr) {
        this.plr = plr;
    }

    public String getPlsj() {
        return plsj;
    }

    public void setPlsj(String plsj) {
        this.plsj = plsj;
    }

    public Integer getNm() {
        return nm;
    }

    public void setNm(Integer nm) {
        this.nm = nm;
    }

    public Integer getDocid() {
        return docid;
    }

    public void setDocid(Integer docid) {
        this.docid = docid;
    }

    public CommentReview getCommentReview() {
        return commentReview;
    }

    public void setCommentReview(CommentReview commentReview) {
        this.commentReview = commentReview;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMessagerurl() {
        return messagerurl;
    }

    public void setMessagerurl(String messagerurl) {
        this.messagerurl = messagerurl;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}

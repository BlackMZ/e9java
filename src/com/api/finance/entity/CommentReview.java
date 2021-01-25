package com.api.finance.entity;

/**
 * @author Arlo
 * @version 1.0.0
 * @Description 制度评论回复
 * @ClassName CommentReview.java
 * @createTime 2021年01月03日 10:46:00
 */
public class CommentReview {
    /**
     * id
     */
    private Integer id;
    /**
     * 评论id
     */
    private Integer plid;
    /**
     * 回复内容
     */
    private String hfnr;
    /**
     * 回复时间
     */
    private String hfsj;
    /**
     * 回复人
     */
    private Integer hfr;

    /** 用户名 */
    private String lastName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlid() {
        return plid;
    }

    public void setPlid(Integer plid) {
        this.plid = plid;
    }

    public String getHfnr() {
        return hfnr;
    }

    public void setHfnr(String hfnr) {
        this.hfnr = hfnr;
    }

    public String getHfsj() {
        return hfsj;
    }

    public void setHfsj(String hfsj) {
        this.hfsj = hfsj;
    }

    public Integer getHfr() {
        return hfr;
    }

    public void setHfr(Integer hfr) {
        this.hfr = hfr;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

package com.api.finance.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arlo
 * @version 1.0.0
 * @Description
 * @ClassName TreeNode.java
 * @createTime 2020年12月30日 14:46:00
 */
public class TreeNode {
    protected int id;
    protected int parentId;
    protected int seccategorytype;
    protected String categoryname;
    protected List<TreeNode> children = new ArrayList<>();

    public void add(TreeNode node) {
        children.add(node);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getSeccategorytype() {
        return seccategorytype;
    }

    public void setSeccategorytype(int seccategorytype) {
        this.seccategorytype = seccategorytype;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }
}

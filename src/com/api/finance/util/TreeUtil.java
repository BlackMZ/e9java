package com.api.finance.util;

import com.api.finance.entity.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arlo
 * @version 1.0.0
 * @Description
 * @ClassName TreeUtil.java
 * @createTime 2020年12月30日 14:50:00
 */
public class TreeUtil {
    /**
     * 使用递归方法建树
     */
    public static <T extends TreeNode> List<T> buildByRecursive(List<T> treeNodes,Object root) {
        List<T> trees = new ArrayList<T>();
        for (T treeNode : treeNodes) {
            if (root.equals(treeNode.getParentId())) {
                trees.add(findChildren(treeNode, treeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     */
    public static <T extends TreeNode> T findChildren(T treeNode, List<T> treeNodes) {
        for (T it : treeNodes) {
            if (treeNode.getId() == it.getParentId()) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<TreeNode>());
                }
                treeNode.add(findChildren(it, treeNodes));
            }
        }
        return treeNode;
    }
}

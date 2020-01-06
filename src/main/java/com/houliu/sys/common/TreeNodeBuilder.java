package com.houliu.sys.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author houliu
 * @create 2019-12-30 17:52
 */

public class TreeNodeBuilder {

    /**
     * 把没有层级关系的集合变成有层级关系的
     * @param treeNodes
     * @param topPid
     * @return
     */
    public static List<TreeNode> build(List<TreeNode> treeNodes,Integer topPid){
        List<TreeNode> nodes = new ArrayList<>();
        for (TreeNode n1 : treeNodes) {
            if (n1.getPid() == topPid){
                nodes.add(n1);
            }
            for (TreeNode n2 : treeNodes) {
                if (n1.getId() == n2.getPid()){
                    n1.getChildren().add(n2);
                }
            }
        }

        return nodes;
    }

}

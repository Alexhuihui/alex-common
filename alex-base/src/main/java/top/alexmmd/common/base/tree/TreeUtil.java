/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package top.alexmmd.common.base.tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 树形结构生成处理工具类
 */
@SuppressWarnings("rawtypes")
public class TreeUtil<T extends TreeNode> {

    /**
     * 两层循环实现建树
     *
     * @param treeNodes  节点集合
     * @param root       根节点ID
     * @param comparator
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends TreeNode> List<T> build(List<T> treeNodes, Object root,
            Comparator comparator) {

        List<T> trees = new ArrayList<T>();
        for (T treeNode : treeNodes) {

            if (root.equals(treeNode.getParentId())) {
                trees.add(treeNode);
            }
            for (T it : treeNodes) {
                if (it.getParentId().equals(treeNode.getId())) {
                    if (treeNode.getChildren() == null) {
                        treeNode.setChildren(new ArrayList<>());
                    }
                    treeNode.add(it);
                }
            }
            if (comparator != null) {
                treeNode.getChildren().sort(comparator);
            }
        }
        return trees;
    }

    /**
     * 使用递归方法建树
     *
     * @param root      根结点ID
     * @param treeNodes 节点集合
     * @return
     */
    public static <T extends TreeNode> List<T> buildByRecursive(Object root, List<T> treeNodes) {
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
     *
     * @param treeNode 父节点
     * @param nodeList 节点列表
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends TreeNode> T findChildren(T treeNode, List<T> nodeList) {
        for (T it : nodeList) {
            if (treeNode.getId().equals(it.getParentId())) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<TreeNode>());
                }
                treeNode.add(findChildren(it, nodeList));
            }
        }
        return treeNode;
    }

}

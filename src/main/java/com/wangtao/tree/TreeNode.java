package com.wangtao.tree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wangtao
 * Created at 2024-07-02
 */
public class TreeNode<T extends TreeNodeElement> {

    /**
     * 将元素中的属性展开到当前节点中
     */
    @JsonUnwrapped
    private final T element;

    @JsonIgnore
    private TreeNode<T> parent;

    private final List<TreeNode<T>> children;

    private TreeNode(T element) {
        this.element = element;
        this.children = new ArrayList<>();
    }

    public void addChild(TreeNode<T> child) {
        this.children.add(child);
    }

    public void addChildren(List<TreeNode<T>> children) {
        this.children.addAll(children);
    }

    public void setChildren(List<TreeNode<T>> children) {
        Objects.requireNonNull(children, "children must not be null!");
        this.children.clear();
        this.children.addAll(children);
    }

    public List<TreeNode<T>> getChildren() {
        return Collections.unmodifiableList(this.children);
    }

    public T getElement() {
        return element;
    }

    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public static <T extends TreeNodeElement> TreeNode<T> of(T element) {
        return new TreeNode<>(element);
    }

    /**
     * 构建树
     * O(n)时间复杂度
     * @param elements 元素列表
     * @return 树节点列表
     */
    public static <T extends TreeNodeElement> List<TreeNode<T>> build(List<T> elements) {
        Objects.requireNonNull(elements, "elements must not be null!");
        List<TreeNode<T>> treeNodes = new ArrayList<>();
        Map<String, TreeNode<T>> treeNodeMap = elements.stream()
                .collect(Collectors.toMap(TreeNodeElement::getKey, TreeNode::of));
        for (T element : elements) {
            TreeNode<T> self = treeNodeMap.get(element.getKey());
            TreeNode<T> parent = treeNodeMap.get(element.getParentKey());
            if (Objects.nonNull(parent)) {
                self.setParent(parent);
                parent.addChild(self);
            } else {
                // 顶级节点
                treeNodes.add(self);
            }
        }
        return treeNodes;
    }

    public static <T extends TreeNodeElement> List<TreeNode<T>> buildByRecursion(List<T> elements) {
        return buildByRecursion(elements, null);
    }

    /**
     * 递归构建树(不推荐，时间复杂度高)
     */
    public static <T extends TreeNodeElement> List<TreeNode<T>> buildByRecursion(List<T> elements, String parentKey) {
        Objects.requireNonNull(elements, "elements must not be null!");
        List<TreeNode<T>> treeNodes = new ArrayList<>();
        for (T element : elements) {
            if (Objects.equals(element.getParentKey(), parentKey)) {
                TreeNode<T> treeNode = TreeNode.of(element);
                List<TreeNode<T>> children = buildByRecursion(elements, element.getKey());
                treeNode.addChildren(children);
                treeNodes.add(treeNode);
            }
        }
        return treeNodes;
    }
}

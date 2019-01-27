package com.wangtao.xml.xpath;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author wangtao
 * Created at 2019/1/10 9:32
 */
public class XNode {

    /**
     * 节点名字
     */
    private String name;

    /**
     * 节点属性集合
     */
    private Properties attributes;

    /**
     * 节点的文本内容, 所有后代节点文本内容的拼接.
     */
    private String textContent;

    /**
     * 当前节点
     */
    private Node node;

    private XpathParser parser;

    public XNode(Node node, XpathParser parser) {
        this.node = node;
        this.name = node.getNodeName();
        this.parser = parser;
        this.attributes = parserAttributes(node);
        this.textContent = parseBody(node);
    }

    /**
     * 获取所有孩子节点(不包括孙子)
     * @return 孩子节点列表
     */
    public List<XNode> getChildren() {
        List<XNode> xNodes = new ArrayList<>();
        NodeList children = this.node.getChildNodes();
        if (children != null) {
            for (int i = 0; i < children.getLength(); i++) {
                Node node = children.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    xNodes.add(new XNode(node, parser));
                }
            }
        }
        return xNodes;
    }

    /**
     * 根据XPath表达式定位后代节点
     * @param expression XPath表达式
     * @return 符合XPath表达式的后代节点
     */
    public XNode evalNode(String expression) {
        return parser.evalNode(expression, node);
    }

    /**
     * 根据XPath表达式定位后代节点集合
     * @param expression XPath表达式
     * @return 符合XPath表达式的后代节点列表
     */
    public List<XNode> evalNodes(String expression) {
        return parser.evalNodes(expression, node);
    }

    public String getStringAttribute(String attrName) {
        return getStringAttribute(attrName, null);
    }

    /**
     * 获取元素节点指定属性名的值, 并转化成字符串形式.
     * 如果不存在返回默认值.
     * @param attrName     属性名
     * @param defaultValue 默认值
     * @return 属性值
     */
    public String getStringAttribute(String attrName, String defaultValue) {
        return attributes.getProperty(attrName, defaultValue);
    }

    public Integer getIntegerAttribute(String attrName) {
        return getIntegerAttribute(attrName, null);
    }

    /**
     * 获取元素节点指定属性名的值, 并转化成整数.
     * 如果不存在返回默认值.
     * @param attrName     属性名
     * @param defaultValue 默认值
     * @return 属性值
     */
    public Integer getIntegerAttribute(String attrName, Integer defaultValue) {
        String value = attributes.getProperty(attrName);
        if (value == null) {
            return defaultValue;
        }
        return Integer.valueOf(value);
    }

    public Boolean getBooleanAttribute(String attrName) {
        return getBooleanAttribute(attrName, null);
    }

    /**
     * 获取节点名字
     */
    public String getName() {
        return name;
    }

    /**
     * 获取文本内容
     */
    public String getTextContent() {
        return textContent;
    }

    /**
     * 获取元素节点指定属性名的值, 并转化成Boolean值.
     * 如果不存在返回默认值.
     * @param attrName     属性名
     * @param defaultValue 默认值
     * @return 属性值
     */
    public Boolean getBooleanAttribute(String attrName, Boolean defaultValue) {
        String value = attributes.getProperty(attrName);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.valueOf(value);
    }

    private Properties parserAttributes(Node node) {
        Properties properties = new Properties();
        // 如果不是元素节点将返回null
        NamedNodeMap attributes = node.getAttributes();
        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attr = attributes.item(i);
                properties.put(attr.getNodeName(), attr.getNodeValue());
            }
        }
        return properties;
    }

    private String parseBody(Node node) {
        return node.getTextContent();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<");
        builder.append(name);
        for (Map.Entry<Object, Object> entry : attributes.entrySet()) {
            builder.append(" ");
            builder.append(entry.getKey());
            builder.append("=\"");
            builder.append(entry.getValue());
            builder.append("\"");
        }
        List<XNode> children = getChildren();
        if (!children.isEmpty()) {
            builder.append(">\n");
            for (XNode node : children) {
                builder.append(node.toString());
            }
            builder.append("</");
            builder.append(name);
            builder.append(">");
        } else if (textContent != null) {
            builder.append(">");
            builder.append(textContent);
            builder.append("</");
            builder.append(name);
            builder.append(">");
        } else {
            builder.append("/>");
        }
        builder.append("\n");
        return builder.toString();
    }
}

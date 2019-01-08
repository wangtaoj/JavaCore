package com.wangtao.xml.test;

import com.wangtao.xml.dom.DtdEntityResolver;
import com.wangtao.xml.dom.XmlResolver;
import com.wangtao.xml.io.Resources;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.*;
import org.xml.sax.EntityResolver;

import java.io.IOException;


/**
 * Created by wangtao at 2019/1/4 22:06
 */
public class DocumentTest {

    @Test
    public void documentAPI() throws IOException {
        XmlResolver xmlResolver = new XmlResolver(Resources.getResourceAsPath("com/wangtao/xml/test/node_test1.xml"), true);
        Document document = xmlResolver.getDocument();
        // 获取XML文档头部编码
        Assert.assertEquals("utf-8", document.getXmlEncoding());

        // 获取根节点
        Element root = document.getDocumentElement();
        Assert.assertNotNull(root);

        // 获取节点名字
        Assert.assertEquals("employee", root.getTagName());
        // 继承自超级接口(Node), 此方法返回值与节点类型相关, 元素节点返回的就是元素节点名称
        Assert.assertEquals("employee", root.getNodeName());
        /*
         * 继承自超级接口(Node), 此方法返回值与节点类型相关.
         * 元素节点返回null
         * 属性节点返回属性值
         * 文本节点返回文本内容
         */
        Assert.assertNull(root.getNodeValue());
        /*
         * 继承自超级接口(Node), 此方法返回值与节点类型相关, 获取所有属性.
         * 元素节点返回一个装有属性节点的NamedNodeMap对象
         * 其它类型节点返回null
         */
        NamedNodeMap attributes = root.getAttributes();
        Assert.assertEquals(1, attributes.getLength());
        // 获取第一个属性节点的属性值
        Assert.assertEquals("${id_var}", attributes.item(0).getNodeValue());

        // 获取节点指定属性节点值
        Assert.assertEquals("${id_var}", root.getAttribute("id"));
        // 获取指定属性节点
        Attr idAttr = root.getAttributeNode("id");
        Assert.assertEquals("id", idAttr.getName());
        Assert.assertEquals("${id_var}", idAttr.getValue());

        NodeList nodeList = root.getElementsByTagName("birth_date");
        Assert.assertEquals(1, nodeList.getLength());
        Element birthday = (Element)nodeList.item(0);
        NodeList childList = birthday.getChildNodes();
        Assert.assertEquals(3, childList.getLength());
        Element yearElement = (Element) childList.item(0);
        Assert.assertEquals("year", yearElement.getTagName());
        // 返回自身及其所有后代元素节点文本内容的拼接
        Assert.assertEquals("1970", yearElement.getTextContent());

        NodeList textNodes = yearElement.getChildNodes();
        Assert.assertEquals(1, textNodes.getLength());
        Text text = (Text) textNodes.item(0);
        Assert.assertEquals("#text", text.getNodeName());
        Assert.assertEquals("1970", text.getWholeText());
    }

    /**
     * 不通过流的方式来解析XML文档
     * 引用的DTD文件无论是在本地还是网络位置都可以
     */
    @Test
    public void validate1() throws IOException {
        new XmlResolver(Resources.getResourceAsPath("com/wangtao/xml/test/mybatis-config.xml"), true);
    }

    /**
     * 通过流的方式来解析XML文档
     * 引用的DTD文件在本地, 需要指定EntityRelover对象
     * 引用的DTD文件在网络, 可以不指定.
     */
    @Test
    public void validate2() throws IOException {
        new XmlResolver(Resources.getResourceAsReader("com/wangtao/xml/test/mybatis-config.xml"), true, null);
    }

    @Test
    public void validate3() throws IOException {
        EntityResolver entityResolver = new DtdEntityResolver("com/wangtao/xml/test/mybatis-3-config.dtd");
        new XmlResolver(Resources.getResourceAsReader("com/wangtao/xml/test/mybatis-config.xml"), true, entityResolver);
    }

    @Test
    public void validate4() throws IOException {
        EntityResolver entityResolver = new DtdEntityResolver("com/wangtao/xml/test/node_test1.dtd");
        new XmlResolver(Resources.getResourceAsReader("com/wangtao/xml/test/node_test1.xml"), true, entityResolver);
    }
}

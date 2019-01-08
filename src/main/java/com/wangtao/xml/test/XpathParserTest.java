package com.wangtao.xml.test;

import com.wangtao.xml.dom.DtdEntityResolver;
import com.wangtao.xml.io.Resources;
import com.wangtao.xml.xpath.XpathParser;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.Reader;

/**
 * Created by wangtao at 2019/1/5 17:16
 */
public class XpathParserTest {

    @Test
    public void initXpathParser() throws Exception {
        Reader reader = Resources.getResourceAsReader("com/wangtao/xml/test/node_test1.xml");
        EntityResolver entityResolver = new DtdEntityResolver("com/wangtao/xml/test/node_test1.dtd");
        XpathParser parser = XpathParser.createBuilder()
                .setReader(reader).setValidating(true).setEntityResolver(entityResolver).build();
        Element root = parser.getRootElement();
        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList nodeList = (NodeList) xpath.evaluate("birth_date", root, XPathConstants.NODESET);
        for(int i = 0; i < nodeList.getLength(); i++) {
            System.out.println(nodeList.item(i).getNodeName());
        }

        Node node = (Node) xpath.evaluate("/employee/blah", parser.getDocument(), XPathConstants.NODE);
        System.out.println(node.getNodeName());

        Node node1 = (Node) xpath.evaluate("/", node, XPathConstants.NODE);
        System.out.println(node1.getNodeName());
    }
}

package com.wangtao.xml.test;

import com.wangtao.xml.dom.DtdEntityResolver;
import com.wangtao.xml.io.Resources;
import com.wangtao.xml.xpath.XpathParser;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.Reader;

import static org.junit.Assert.assertEquals;

/**
 * Created by wangtao at 2019/1/5 17:16
 */
public class XpathParserTest {

    @Test
    public void initXpathParser() throws Exception {
        String resource = "com/wangtao/xml/test/node_test1.xml";
        try(Reader reader = Resources.getResourceAsReader(resource)) {
            EntityResolver entityResolver = new DtdEntityResolver("com/wangtao/xml/test/node_test1.dtd");
            XpathParser parser = new XpathParser.Builder().reader(reader)
                    .validating(true).entityResolver(entityResolver).build();
            Document document = parser.getDocument();
            XPath xpath = XPathFactory.newInstance().newXPath();
            /*
             * 1. XPath中'/' 代表根路径, 即文档对象
             * 2. 根元素用/<root_element_name>表示
             * 3. 不是以根路径开头, 那就是相对路径
             */
            Node node1 = (Node) xpath.evaluate("/", document, XPathConstants.NODE);
            Assert.assertEquals(Node.DOCUMENT_NODE, node1.getNodeType());

            // 相对根路径没有birth_date节点
            NodeList nodeList = (NodeList) xpath.evaluate("birth_date", document, XPathConstants.NODESET);
            Assert.assertEquals(0, nodeList.getLength());

            // 获取根元素节点<employee>
            Node root = (Node) xpath.evaluate("/employee", document, XPathConstants.NODE);
            Assert.assertEquals("employee", root.getNodeName());

            // 使用相对路径定位
            Node birthDay = (Node) xpath.evaluate("birth_date", root, XPathConstants.NODE);
            Assert.assertEquals("birth_date", birthDay.getNodeName());

            // 使用绝对路径定位
            Node birthDay1 = (Node) xpath.evaluate("/employee/birth_date", root, XPathConstants.NODE);
            Assert.assertEquals("birth_date", birthDay1.getNodeName());
        }
    }

    @Test
    public void xPathParserAPI() throws IOException  {
        String resource = "com/wangtao/xml/test/node_test1.xml";
        try (Reader reader = Resources.getResourceAsReader(resource)) {
            EntityResolver entityResolver = new DtdEntityResolver("com/wangtao/xml/test/node_test1.dtd");
            XpathParser parser = new XpathParser.Builder().reader(reader)
                    .validating(true).entityResolver(entityResolver).build();
            assertEquals(Integer.valueOf(1970), parser.evalInteger("/employee/birth_date/year/text()"));
            assertEquals((Integer) 15, parser.evalInteger("/employee/birth_date/day"));
            assertEquals((Double) 5.8d, parser.evalDouble("/employee/height"));
            assertEquals("${id_var}", parser.evalString("/employee/@id"));
            assertEquals(Boolean.TRUE, parser.evalBoolean("/employee/active"));
            assertEquals("<id>${id_var}</id>", parser.evalNode("/employee/@id").toString().trim());
            assertEquals(7, parser.evalNodes("/employee/*").size());
        }
    }
}

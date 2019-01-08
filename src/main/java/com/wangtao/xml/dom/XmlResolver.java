package com.wangtao.xml.dom;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.Reader;

/**
 * 节点类型
 * 元素节点、属性节点、文本节点、注释节点、CDATA片段等
 * CDATA片段中的文本会被解析器忽略, 对于一些转义字符就可以写在CDATA片段中.
 * 常见的转义字符如下:
 * < 对应 &lt;  > 对应 &gt;  & 对应 &amp;
 * <bookStore>
 *     <book category="计算机">
 *         <name>JAVA编程思想</name>
 *         <description>
 *             <![CDATA[
 *                  历史最低价, 当前价格已经<100元
 *             ]]>
 *         </description>
 *     </book>
 * </bookStore>
 * 元素节点: <bookStore>、<book>等
 * 文本节点: <bookStore>元素与<book>之间的空白符、JAVA编程思想
 * 属性节点: category="计算机"
 *
 * Created by wangtao at 2019/1/4 20:49
 */
public class XmlResolver {

    /**
     * 文档对象
     */
    private final Document document;

    /**
     * 是否根据DTD文件来校验文档的正确性
     */
    private boolean validating;
    /**
     * 是否忽略元素之间的空白符(设置为true时, 需要XML文档中定义约束文件(DTD, SCHEMA)才会生效
     * 例子:
     * <books>
     *     <book>DOM</book>
     * <books/>
     * books元素与book元素之间的空白符会忽略.
     * 不忽略books将有3个子元素  空白符1(文本节点), book元素, 空白符2(文本节点)
     * 默认为true
     */
    private boolean whitespace = true;

    /**
     * 当使用字节流或者字符流来解析文档对象时,
     * XML文档引用的外部实体需要通过EntityResolver对象来定位(比如DTD约束文件)
     * 注意: 只有DTD文件路径是相对(本地)的才需要EntityResolver
     * 如果是网络位置, 也可以不用EntityResolver
     *
     * 需要使用EntityResolver的理由:
     * 1. 通过流来解析XML文档, 并且文档有引用外部资源, 该资源在本地.
     * 2. 引用的外部资源是网络位置, 但是需要XML解析器不去网络找, 加快解析速度, 防止网络原因超时,
     * 告知解析器去本地找该引用的资源.
     */
    private EntityResolver entityResolver;


    /**
     * 初始化XmlResolver对象, 不开启校验
     * @param resource 资源位置
     */
    public XmlResolver(String resource) {
        this(resource, false);
    }

    /**
     * 初始化XmlResolver对象
     * @param resource 资源位置
     * @param validating 是否校验XML文档
     */
    public XmlResolver(String resource, boolean validating) {
        this(new InputSource(resource), validating, null);
    }

    /**
     * 初始化XmlResolver对象, 不开启校验
     * @param reader 资源对应的字符流
     */
    public XmlResolver(Reader reader) {
        this(new InputSource(reader), false, null);
    }

    public XmlResolver(Reader reader, boolean validating, EntityResolver entityResolver) {
        this(new InputSource(reader), validating, entityResolver);
    }

    public XmlResolver(InputSource inputSource, boolean validating, EntityResolver entityResolver) {
        this.validating = validating;
        this.entityResolver = entityResolver;
        this.document = createDocument(inputSource);
    }

    /**
     * 根据节点名字获取所有节点.
     * @param tagName 节点名字
     * @return 节点列表
     */
    public NodeList getElementsByTagName(String tagName) {
        return document.getElementsByTagName(tagName);
    }

    private Document createDocument(InputSource inputSource) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // 是否验证文档(根据DTD约束)
            factory.setValidating(validating);
            // 忽略元素之间的空白符(节点值空白符不会忽略)
            factory.setIgnoringElementContentWhitespace(whitespace);
            // 忽略注释
            factory.setIgnoringComments(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(entityResolver);
            // 如果开启验证, 需要设置错误处理器来输出错误.
            builder.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException exception) {
                    // just ignore warning
                }

                @Override
                public void error(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    throw exception;
                }
            });
            return builder.parse(inputSource);
        } catch (Exception e) {
            throw new RuntimeException("创建文档对象发生错误, 原因:" + e);
        }
    }

    public Document getDocument() {
        return document;
    }

    public boolean isValidating() {
        return validating;
    }

    public void setValidating(boolean validating) {
        this.validating = validating;
    }

    public boolean isWhitespace() {
        return whitespace;
    }

    public void setWhitespace(boolean whitespace) {
        this.whitespace = whitespace;
    }

    public EntityResolver getEntityResolver() {
        return entityResolver;
    }

    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }
}

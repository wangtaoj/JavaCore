package com.wangtao.xml.xpath;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.*;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.Reader;

/**
 * Created by wangtao at 2019/1/5 17:00
 */
public class XpathParser {

    private final Document document;

    private boolean validating;

    private EntityResolver entityResolver;

    private XPath xpath;

    private XpathParser(InputSource inputSource, boolean validating, EntityResolver entityResolver) {
        this.validating = validating;
        this.entityResolver = entityResolver;
        this.document = createDocument(inputSource);
        this.xpath = XPathFactory.newInstance().newXPath();
    }

    public String evalString(String expression) {
        return evalString(expression, document);
    }

    public String evalString(String expression, Object root) {
        return (String) evaluate(expression, root, XPathConstants.STRING);
    }

    public Boolean evalBoolean(String expression) {
        return evalBoolean(expression, document);
    }

    public Boolean evalBoolean(String expression, Object root) {
        return (Boolean) evaluate(expression, root, XPathConstants.BOOLEAN);
    }

    public Integer evalInteger(String expression) {
        return evalInteger(expression, document);
    }

    public Integer evalInteger(String expression, Object root) {
        return Integer.valueOf(evalString(expression, root));
    }

    public Double evalDouble(String expression) {
        return evalDouble(expression, document);
    }

    public Double evalDouble(String expression, Object root) {
        return Double.valueOf(evalString(expression, root));
    }

    private Object evaluate(String expression, Object root, QName qName) {
        try {
            return xpath.evaluate(expression, root, qName);
        } catch (XPathExpressionException e) {
            throw new RuntimeException("解析XPath表达式出现错误, 原因: " + e);
        }
    }

    public Document getDocument() {
        return document;
    }

    public Element getRootElement() {
        return document.getDocumentElement();
    }

    private Document createDocument(InputSource inputSource) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // 是否验证文档(根据DTD约束)
            factory.setValidating(validating);
            // 忽略元素之间的空白符(节点值空白符不会忽略)
            factory.setIgnoringElementContentWhitespace(true);
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

    public static XpathParserBuilder createBuilder() {
        return new XpathParserBuilder();
    }


    public static class XpathParserBuilder {

        private boolean validating;

        private EntityResolver entityResolver;

        private Reader reader;

        private String resource;

        public XpathParserBuilder setValidating(boolean validating) {
            this.validating = validating;
            return this;
        }

        public XpathParserBuilder setEntityResolver(EntityResolver entityResolver) {
            this.entityResolver = entityResolver;
            return this;
        }

        public XpathParserBuilder setReader(Reader reader) {
            this.reader = reader;
            return this;
        }

        public XpathParserBuilder setResource(String resource) {
            this.resource = resource;
            return this;
        }

        public XpathParser build() {
            if(reader != null) {
                return new XpathParser(new InputSource(reader), validating, entityResolver);
            } else if(resource != null) {
                return new XpathParser(new InputSource(resource), validating, entityResolver);
            }
            throw new RuntimeException("请指定一个字符流或者资源位置");
        }
    }
}

package com.wangtao.xml.dom;

import com.wangtao.xml.io.Resources;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by wangtao at 2019/1/5 15:12
 */
public class DtdEntityResolver implements EntityResolver {

    private String localPosition;

    public DtdEntityResolver(String localPosition) {
        this.localPosition = localPosition;
    }

    /**
     * 将读取网络位置的文件转向读取本地.
     * 返回null, 将采取默认行为.
     * @param publicId "PUBLIC" 后的字符串
     * @param systemId publicId后的字符串, 代表文件的真实位置.
     * @return InputSource对象
     * @throws SAXException 任何错误发生时抛出
     */
    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
        try {
            if(systemId != null) {
                return getInputSource(publicId, systemId);
            }
            return null;
        } catch (Exception e) {
            throw new SAXException(e.getMessage());
        }
    }

    private InputSource getInputSource(String publicId, String systemId) {
        InputSource source = null;
        try {
            Reader reader = Resources.getResourceAsReader(localPosition);
            source = new InputSource(reader);
            source.setPublicId(publicId);
            source.setSystemId(systemId);
        } catch (IOException e) {
            // just do nothing
        }
        return source;
    }
}

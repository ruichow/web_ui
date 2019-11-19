package com.hand.api;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;

import java.nio.charset.UnsupportedCharsetException;

/**
 * @author lei.shi06@hand-china.com
 * @see ContentType
 */
public class HttpContentType {

    // application/json UTF-8
    public static final ContentType APPLICATION_JSON_UTF_8 = ContentType.APPLICATION_JSON;
    // application/xml ISO-8859-1
    public static final ContentType APPLICATION_XML = ContentType.APPLICATION_XML;
    public static final ContentType APPLICATION_XML_UTF_8 = ContentType.create("application/xml", "UTF-8");
    public static final ContentType TEXT_HTML_UTF_8 = ContentType.create("text/html", Consts.UTF_8);
    public static final ContentType TEXT_PLAIN_UTF_8 = ContentType.create("text/plain", Consts.UTF_8);
    public static final ContentType TEXT_XML_UTF_8 = ContentType.create("text/xml", Consts.UTF_8);

    public static ContentType getOrDefault(final HttpEntity entity) throws ParseException, UnsupportedCharsetException {
        ContentType contentType = ContentType.get(entity);
        return contentType == null ? TEXT_PLAIN_UTF_8 : contentType;
    }
}

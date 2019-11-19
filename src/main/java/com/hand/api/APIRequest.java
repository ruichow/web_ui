package com.hand.api;

import com.hand.api.exception.APIException;
import com.google.gson.GsonBuilder;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * The Default Charset is UTF-8
 *
 * @author gulinrang
 * @see HttpContentType
 */
public class APIRequest {

    private final HttpUriRequest httpRequest;
    public String body;
    private URI serviceURI;

    public APIRequest(HttpUriRequest httpRequest) {
        super();
        this.serviceURI = httpRequest.getURI();
        this.httpRequest = httpRequest;
    }

    /*
    public HttpGet(URI uri)
    public HttpGet(String uri)
     */
    public static APIRequest get(final URI uri) {
        return new APIRequest(new HttpGet(uri));
    }

    public static APIRequest get(final String uri) {
        return new APIRequest(new HttpGet(uri));
    }

    /*
     NameValuePair 简单名称值对节点类型
     1、建立一个NameValuePair数组，用于存储传送的数据
     List<NameValuePair> params=new ArrayList<NameValuePair>();
     2、添加参数
     params.add(new BasicNameValuePair("键","值"));
     */
    public static APIRequest get(final String uri, final Iterable<? extends NameValuePair> nameValuePairs) {
        final List<NameValuePair> paramList = new ArrayList<>();
        for (NameValuePair param : nameValuePairs) {
            paramList.add(param);
        }
        // 参数转换为字符串
        String paramStr = null;
        try {
            paramStr = EntityUtils.toString(new UrlEncodedFormEntity(paramList, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new APIException(e);
        }
        String url = uri + "?" + paramStr;
        return new APIRequest(new HttpGet(url));
    }

    /*
     URIBuilder:

     String url = "http://info.sporttery.cn/football/info/fb_match_hhad.php?m=102909";
     URIBuilder uriBuilder = new URIBuilder(url);
     ArrayList<NameValuePair> objects = new ArrayList<>();
     ArrayList<NameValuePair> objects1 = new ArrayList<>();
     NameValuePair m = new BasicNameValuePair("m", "1");
     objects.add(m);
     NameValuePair m1 = new BasicNameValuePair("m", "8");
     objects1.add(m1);
     uriBuilder.setParameters(objects);
     uriBuilder.addParameters(objects1);
     System.out.println(uriBuilder.build());
     输出：http://info.sporttery.cn/football/info/fb_match_hhad.php?m=1&m=8
     得出结论：set会覆盖原来的同名参数而add不会
     */
    public static APIRequest get(final URIBuilder uriBuilder) throws URISyntaxException {
        return new APIRequest(new HttpGet(uriBuilder.build()));
    }

    public static APIRequest post(final URI uri) {
        return new APIRequest(new HttpPost(uri));
    }

    public static APIRequest post(final String uri) {
        return new APIRequest(new HttpPost(uri));
    }

    public static APIRequest put(final URI uri) {
        return new APIRequest(new HttpPut(uri));
    }

    public static APIRequest put(final String uri) {
        return new APIRequest(new HttpPut(uri));
    }

    public static APIRequest delete(final URI uri) {
        return new APIRequest(new HttpDelete(uri));
    }

    public static APIRequest delete(final String uri) {
        return new APIRequest(new HttpDelete(uri));
    }

    public static APIRequest head(final URI uri) {
        return new APIRequest(new HttpHead(uri));
    }

    public static APIRequest head(final String uri) {
        return new APIRequest(new HttpHead(uri));
    }


    public APIRequest addHeader(final String name, final String value) {
        httpRequest.addHeader(name, value);
        return this;
    }

    public APIRequest setHeader(final String name, final String value) {
        httpRequest.setHeader(name, value);
        return this;
    }

    public APIRequest removeHeader(final String name) {
        httpRequest.removeHeaders(name);
        return this;
    }

    public APIRequest setUserAgent(final String agent) {
        this.httpRequest.setHeader(HTTP.USER_AGENT, agent);
        return this;
    }

    public APIRequest setConnectionRequestTimeout(int seconds) {
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        requestConfigBuilder.setConnectionRequestTimeout(seconds * 1000);
        ((HttpRequestBase) httpRequest).setConfig(requestConfigBuilder.build());
        return this;
    }


    /*
    UrlEncodedFormEntity:

    List<NameValuePair> pairs = new ArrayList<NameValuePair>();

    NameValuePair pair1 = new BasicNameValuePair("supervisor", supervisorEt.getEditableText().toString());
    NameValuePair pair2 = new BasicNameValuePair("content", superviseContentEt.getEditableText().toString());
    NameValuePair pair3 = new BasicNameValuePair("userId", String.valueOf(signedUser.getId()));

    pairs.add(pair1);
    pairs.add(pair2);
    pairs.add(pair3);

    httpPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8))
     */

    /*
    StringEntity:

    JSONObject postData = new JSONObject();

    postData.put("supervisor", supervisorEt.getEditableText().toString());
    postData.put("content", superviseContentEt.getEditableText().toString());
    postData.put("userId", signedUser.getId());

    httpPost.setEntity(new StringEntity(postData.toString(), HTTP.UTF_8));
     */

    /*
    UrlEncodedFormEntity()的形式比较单一，只能是普通的键值对，局限性相对较大。
    而StringEntity()的形式比较自由，只要是字符串放进去，不论格式都可以。
     */

    public APIRequest setBodyEntity(final HttpEntity entity) {
        if (this.httpRequest instanceof HttpEntityEnclosingRequest) {
            ((HttpEntityEnclosingRequest) this.httpRequest).setEntity(entity);
        } else {
            throw new IllegalStateException(this.httpRequest.getMethod() + " request cannot enclose an entity");
        }
        if (entity instanceof StringEntity) {
            try {
                this.body = EntityUtils.toString(entity);
            } catch (ParseException | IOException e) {
                throw new APIException(e);
            }
        }
        return this;
    }

    public APIRequest setBodyEntity(String body, ContentType contentType) {
        setBodyEntity(new StringEntity(body, contentType));
        return this;
    }

    public APIRequest setBodyJsonEntity(String entity) {
        setBodyEntity(new StringEntity(entity, HttpContentType.APPLICATION_JSON_UTF_8));
        return this;
    }

    public APIRequest setBodyXmlEntity(String entity) {
        setBodyEntity(new StringEntity(entity, HttpContentType.APPLICATION_XML_UTF_8));
        return this;
    }

    public APIRequest setBodyGsonEntity(Object object) {
        String body = new GsonBuilder().disableHtmlEscaping().create().toJson(object);
//        System.out.println(body);
        setBodyEntity(new StringEntity(body, HttpContentType.APPLICATION_JSON_UTF_8));
        return this;
    }

    public APIRequest setBodyFile(final File file, final ContentType contentType) {
        return setBodyEntity(new FileEntity(file, contentType));
    }

    public APIRequest setBodyForm(final Iterable<? extends NameValuePair> formParams) {
        return setBodyForm(formParams, Consts.UTF_8);
    }

    public APIRequest setBodyForm(final Iterable<? extends NameValuePair> formParams, final Charset charset) {
        final List<NameValuePair> paramList = new ArrayList<>();
        for (NameValuePair param : formParams) {
            paramList.add(param);
        }
        setBodyEntity(new UrlEncodedFormEntity(paramList, charset));
        return this;
    }


    public List<NameValuePair> getUrlParams() {
        URIBuilder newBuilder = new URIBuilder(this.getServiceURI());
        return newBuilder.getQueryParams();
    }

    public URI getServiceURI() {
        return serviceURI;
    }

    public HttpUriRequest getRequest() {
        return httpRequest;
    }

    public String getBody() {
        return body;
    }
}
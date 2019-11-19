package com.hand.api;

import com.hand.api.exception.APIException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.auth.*;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLInitializationException;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import static java.lang.System.lineSeparator;


/**
 * An Executor for fluent requests.
 * <p>
 * A {@link PoolingHttpClientConnectionManager} with maximum 100 connections per
 * route and a total maximum of 200 connections is used internally.
 * </p>
 */
@Slf4j
public class APIDriver {

    private final static PoolingHttpClientConnectionManager CONNMGR;
    private final static HttpClient CLIENT;

    static {
        LayeredConnectionSocketFactory ssl = null;
        try {
            ssl = SSLConnectionSocketFactory.getSystemSocketFactory();
        } catch (final SSLInitializationException ex) {
            final SSLContext sslcontext;
            try {
                sslcontext = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
                sslcontext.init(null, null, null);
                ssl = new SSLConnectionSocketFactory(sslcontext);
            } catch (final SecurityException | KeyManagementException | NoSuchAlgorithmException ignore) {
            }
        }

        final Registry<ConnectionSocketFactory> sfr = RegistryBuilder
                .<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", ssl != null ? ssl : SSLConnectionSocketFactory.getSocketFactory()).build();

        CONNMGR = new PoolingHttpClientConnectionManager(sfr);
        CONNMGR.setDefaultMaxPerRoute(100);
        CONNMGR.setMaxTotal(200);
        CLIENT = HttpClientBuilder.create()
                .addInterceptorLast(new DefaultRequestInterceptor())
                .addInterceptorLast(new DefaultResponseInterceptor())
                .setConnectionManager(CONNMGR).build();
    }

    public static APIDriver newInstance() {
        return new APIDriver(CLIENT);
    }

    public static APIDriver newInstance(final HttpClient httpclient) {
        return new APIDriver(httpclient != null ? httpclient : CLIENT);
    }

    private final HttpClient httpclient;
    private final AuthCache authCache;
    private final CredentialsProvider credentialsProvider;

    private volatile CookieStore cookieStore;

    APIDriver(final HttpClient httpclient) {
        super();
        this.httpclient = httpclient;
        this.credentialsProvider = new BasicCredentialsProvider();
        this.authCache = new BasicAuthCache();
    }

    public APIDriver auth(final AuthScope authScope, final Credentials creds) {
        this.credentialsProvider.setCredentials(authScope, creds);
        return this;
    }

    public APIDriver auth(final HttpHost host, final Credentials creds) {
        final AuthScope authScope = host != null ? new AuthScope(
                host.getHostName(), host.getPort()) : AuthScope.ANY;
        return auth(authScope, creds);
    }

    /**
     * @since 4.4
     */
    public APIDriver auth(final String host, final Credentials creds) {
        return auth(HttpHost.create(host), creds);
    }

    public APIDriver authPreemptive(final HttpHost host) {
        final BasicScheme basicScheme = new BasicScheme();
        try {
            basicScheme.processChallenge(new BasicHeader(AUTH.WWW_AUTH, "BASIC "));
        } catch (final MalformedChallengeException ignore) {
        }
        this.authCache.put(host, basicScheme);
        return this;
    }

    /**
     * @since 4.4
     */
    public APIDriver authPreemptive(final String host) {
        return authPreemptive(HttpHost.create(host));
    }

    public APIDriver authPreemptiveProxy(final HttpHost proxy) {
        final BasicScheme basicScheme = new BasicScheme();
        try {
            basicScheme.processChallenge(new BasicHeader(AUTH.PROXY_AUTH,
                    "BASIC "));
        } catch (final MalformedChallengeException ignore) {
        }
        this.authCache.put(proxy, basicScheme);
        return this;
    }

    /**
     * @since 4.4
     */
    public APIDriver authPreemptiveProxy(final String proxy) {
        return authPreemptiveProxy(HttpHost.create(proxy));
    }

    public APIDriver auth(final Credentials cred) {
        return auth(AuthScope.ANY, cred);
    }

    public APIDriver auth(final String username, final String password) {
        return auth(new UsernamePasswordCredentials(username, password));
    }

    public APIDriver auth(final String username, final String password,
                          final String workstation, final String domain) {
        return auth(new NTCredentials(username, password, workstation, domain));
    }

    public APIDriver auth(final HttpHost host, final String username,
                          final String password) {
        return auth(host, new UsernamePasswordCredentials(username, password));
    }

    public APIDriver auth(final HttpHost host, final String username,
                          final String password, final String workstation, final String domain) {
        return auth(host, new NTCredentials(username, password, workstation,
                domain));
    }

    public APIDriver clearAuth() {
        this.credentialsProvider.clear();
        return this;
    }

    public APIDriver cookieStore(final CookieStore cookieStore) {
        this.cookieStore = cookieStore;
        return this;
    }

    public APIDriver clearCookies() {
        this.cookieStore.clear();
        return this;
    }

    /*
    HttpClientContext:
    适配器类，为HTTP请求执行过程中使用的常见HttpContext属性提供便捷类型安全setter和getter。
     */
    public APIResponse execute(final APIRequest request) {
        final HttpClientContext localContext = HttpClientContext.create();
        localContext.setAttribute(HttpClientContext.CREDS_PROVIDER, this.credentialsProvider);
        localContext.setAttribute(HttpClientContext.AUTH_CACHE, this.authCache);
        localContext.setAttribute(HttpClientContext.COOKIE_STORE, this.cookieStore);

        final HttpUriRequest httpUriRequest = request.getRequest();
        HttpResponse response;

        try {
            response = httpclient.execute(httpUriRequest, localContext);
        } catch (IOException e) {
            throw new APIException(e);
        }

        return new APIResponse(response);
    }

    /**
     * Closes all idle persistent connections used by the internal pool.
     *
     * @since 4.4
     */
    public static void closeIdleConnections() {
        CONNMGR.closeIdleConnections(0, TimeUnit.MICROSECONDS);
    }

    public static class DefaultRequestInterceptor implements HttpRequestInterceptor {
        @Override
        public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
            StringBuilder dump = new StringBuilder();
            HttpHost host = (HttpHost) context.getAttribute(HttpCoreContext.HTTP_TARGET_HOST);

            String uri = host.toURI();
            if (request instanceof HttpUriRequest) {
                dump.append(((HttpUriRequest) request).getMethod())
                        .append(" ")
                        .append(uri)
                        .append(((HttpUriRequest) request).getURI())
                        .append(" ")
                        .append(request.getProtocolVersion())
                        .append(lineSeparator());

                dumpHeader(request, dump);
            }
            if (request instanceof HttpEntityEnclosingRequest) {
                HttpEntityEnclosingRequest entityRequest = (HttpEntityEnclosingRequest) request;
                HttpEntity entity = entityRequest.getEntity();
                if(entity != null) {
                    dump.append(lineSeparator()).append(EntityUtils.toString(entity));
                }
            }
            log.debug(dump.toString());
        }

    }

    public static class DefaultResponseInterceptor implements HttpResponseInterceptor {

        @Override
        public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
            StringBuilder dump = new StringBuilder();
            dump.append("Status-line:")
                    .append(response.getStatusLine().toString())
                    .append(lineSeparator());

            dumpHeader(response, dump);
            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                final ContentType contentType = HttpContentType.getOrDefault(entity);
                final ByteArrayEntity byteArrayEntity = new ByteArrayEntity(EntityUtils.toByteArray(entity));

                byteArrayEntity.setContentType(contentType.toString());
                response.setEntity(byteArrayEntity);
                dump.append(lineSeparator()).append(EntityUtils.toString(byteArrayEntity));
                log.debug(dump.toString());
            }
        }
    }

    private static void dumpHeader(HttpMessage httpMessage, StringBuilder dump) {
        Header[] headers = httpMessage.getAllHeaders();
        for (Header header : headers) {
            dump.append(header.getName())
                    .append(":")
                    .append(header.getValue())
                    .append(lineSeparator());
        }
    }
}

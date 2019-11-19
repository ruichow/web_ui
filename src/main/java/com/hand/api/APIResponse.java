package com.hand.api;

import com.hand.api.exception.APIException;
import com.hand.api.exception.APIResponseException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

//import org.apache.http.client.fluent.Response;

/**
 *
 // * @see Response
 */
public class APIResponse {

    private final HttpResponse response;

    private int statusCode;

    /**
     * APIDriver has responsibillty to consume Response
     */
    private boolean consumed;

    private String body;

    APIResponse(HttpResponse response) {
        this.response = response;
        statusCode = response.getStatusLine().getStatusCode();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getHeaderValue(final String name) {
        return response.getFirstHeader(name).getValue();
    }

    public HttpEntity getEntity() {
        return response.getEntity();
    }

    public Header[] getHeaders() {
        return response.getAllHeaders();
    }

    public JsonElement readGsonElement() {
        innerConsumeContent();
        return new JsonParser().parse(body);
    }

    public <T> T readGsonEntity(Class<T> classOfT) {
        innerConsumeContent();
        return new Gson().fromJson(body, classOfT);
    }

    public String getBody() {
        innerConsumeContent();
        return body;
    }

    private void innerConsumeContent() {
        if (this.consumed) {
            return;
        }
        this.consumed = true;
        final StatusLine statusLine = response.getStatusLine();
        final HttpEntity entity = response.getEntity();
        if (statusLine.getStatusCode() >= 300) {
            throw new APIResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }
        if (entity != null) {
            try {
                this.body = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            } catch (ParseException | IOException e) {
                throw new APIException(e);
            } finally {
                EntityUtils.consumeQuietly(entity);
            }
        }
    }

    public HttpResponse getResponse() {
        assertNotConsumed();
        try {
            final HttpEntity entity = this.response.getEntity();
            if (entity != null) {
                final ByteArrayEntity byteArrayEntity = new ByteArrayEntity(EntityUtils.toByteArray(entity));
                final ContentType contentType = ContentType.getOrDefault(entity);
                byteArrayEntity.setContentType(contentType.toString());
                this.response.setEntity(byteArrayEntity);
            }
            return this.response;
        } catch (IOException e) {
            throw new APIException(e);
        }finally {
            this.consumed = true;
        }
    }

    public void discardContent() {
        dispose();
    }

    private void dispose() {
        if (this.consumed) {
            return;
        }
        try {
            final HttpEntity entity = this.response.getEntity();
            EntityUtils.consumeQuietly(entity);
        } catch (final Exception ignore) {
        } finally {
            this.consumed = true;
        }
    }

    private void assertNotConsumed() {
        if (this.consumed) {
            throw new IllegalStateException(
                    "APIResponse content has been already consumed");
        }
    }
}

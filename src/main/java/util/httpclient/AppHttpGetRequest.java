package util.httpclient;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class AppHttpGetRequest {
    private String url;
    private List<BasicHeader> headers;
    private List<NameValuePair> params;

    private AppHttpGetRequest() {
        headers = new ArrayList<>();
        params = new ArrayList<>();
    }

    public static Builder newBuilder() {
        return new AppHttpGetRequest().new Builder();
    }


    public class Builder {

        private Builder() {
        }

        public Builder setUrl(String url) {
            AppHttpGetRequest.this.url = url;
            return this;
        }

        public Builder setHeaders(List<BasicHeader> headers) {
            AppHttpGetRequest.this.headers = headers;
            return this;
        }

        public Builder addHeader(String name, String value) {
            AppHttpGetRequest.this.headers.add(new BasicHeader(name, value));
            return this;
        }

        public Builder addParam(String name, String value) {
            AppHttpGetRequest.this.params.add(new BasicNameValuePair(name, value));
            return this;
        }

        public AppHttpGetRequest build() {
            return AppHttpGetRequest.this;
        }

    }

    public AppHttpResponse execute() throws Exception {
        URIBuilder uriBuilder = new URIBuilder(url);
        for (NameValuePair param : params) {
            uriBuilder.setParameter(param.getName(), param.getValue());
        }

        HttpGet request = new HttpGet(uriBuilder.build());

        for (BasicHeader header : headers) {
            request.addHeader(header);
        }
        int status = 0;
        List<BasicHeader> responseHeaders = new ArrayList<>();
        byte[] byteArray = null;

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {

            status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();

            for (Header header : response.getAllHeaders()) {
                responseHeaders.add(new BasicHeader(header.getName(), header.getValue()));
            }

            if (entity != null) {
                byteArray = EntityUtils.toByteArray(entity);
                EntityUtils.consume(entity);
            }
            return new AppHttpResponse(status, byteArray, headers);
        }
    }
}

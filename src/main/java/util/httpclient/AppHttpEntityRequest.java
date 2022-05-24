package util.httpclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AppHttpEntityRequest {
    private String url;
    private List<BasicHeader> headers;
    private List<NameValuePair> params;
    private byte[] body;
    private MethodType type;


    private AppHttpEntityRequest() {
        headers = new ArrayList<>();
        params = new ArrayList<>();
    }

    public static Builder newBuilder() {
        return new AppHttpEntityRequest().new Builder();
    }


    public class Builder {

        private Builder() {
        }

        public Builder setMethod(MethodType type) {
            AppHttpEntityRequest.this.type = type;
            return this;
        }

        public Builder setUrl(String url) {
            AppHttpEntityRequest.this.url = url;
            return this;
        }

        public Builder setHeaders(List<BasicHeader> headers) {
            AppHttpEntityRequest.this.headers = headers;
            return this;
        }

        public Builder addHeader(String name, String value) {
            AppHttpEntityRequest.this.headers.add(new BasicHeader(name, value));
            return this;
        }

        public Builder addParam(String name, String value) {
            AppHttpEntityRequest.this.params.add(new BasicNameValuePair(name, value));
            return this;
        }

        @SneakyThrows
        public Builder setBody(Object object) {
            AppHttpEntityRequest.this.body = new ObjectMapper().writeValueAsBytes(object);
            return this;
        }

        public Builder setBody(String jsonStr) {
            AppHttpEntityRequest.this.body = jsonStr.getBytes(StandardCharsets.UTF_8);
            return this;
        }

        public AppHttpEntityRequest build() {
            return AppHttpEntityRequest.this;
        }

    }

    public AppHttpResponse execute() throws Exception {
            URIBuilder uriBuilder = new URIBuilder(url);
            for (NameValuePair param : params) {
                uriBuilder.setParameter(param.getName(), param.getValue());
            }
            HttpEntityEnclosingRequestBase request;
            switch (type) {
                case PATCH:
                    request = new HttpPatch(uriBuilder.build());
                    break;
                default:
                    request = new HttpPost(uriBuilder.build());
                    break;
            }
            for (BasicHeader header : headers) {
                request.addHeader(header);
            }
            request.setEntity(new ByteArrayEntity(body));

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

    public enum MethodType {
        POST,
        PATCH
    }
}

package util.httpclient;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.http.message.BasicHeader;

import java.util.Arrays;
import java.util.List;

public class AppHttpResponse {

    private int status = 0;
    private byte[] byteArray = null;
    private final List<BasicHeader> headers;

    public AppHttpResponse(int status, byte[] byteArray, List<BasicHeader> headers) {
        this.status = status;
        this.byteArray = byteArray;
        this.headers = headers;
    }

    public boolean is200() {
        return status >= 200 && status <= 299;
    }

    public int getStatus() {
        return status;
    }

    public String getString() {
        return new String(this.byteArray);
    }

    public byte[] getRawBytes() {
        return byteArray;
    }

    public List<BasicHeader> getHeaders() {
        return headers;
    }

    @SneakyThrows
    public <T> List<T> getListOf(Class<T[]> dtoClass) {
        ObjectMapper mapper = new ObjectMapper();
        String json = new String(this.byteArray);
        return Arrays.asList(mapper.readValue(json, dtoClass));
    }

    @SneakyThrows
    public <T> T getInstanceOf(Class<T> dtoClass) {
        ObjectMapper mapper = new ObjectMapper();
        String json = new String(this.byteArray);
        return mapper.readValue(json, dtoClass);
    }

    @SneakyThrows
    public <T> T getInstanceWithIgnoreCaseOf(Class<T> dtoClass) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        String json = new String(this.byteArray);
        return mapper.readValue(json, dtoClass);
    }
}

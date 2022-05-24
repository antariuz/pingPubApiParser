package API;

import model.Response;
import org.apache.http.message.BasicHeader;
import util.httpclient.AppHttpGetRequest;
import util.httpclient.AppHttpResponse;

import java.util.Arrays;
import java.util.List;

public class APIImpl implements API {

    private final static List<BasicHeader> customHeaders = Arrays.asList(
            new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0"),
            new BasicHeader("Accept", "*/*"),
            new BasicHeader("Content-Type", "application/json")
    );

    @Override
    public Response getResponse() throws Exception {
        AppHttpResponse response = AppHttpGetRequest.newBuilder()
                .setUrl("https://api.alley.umeemania-1.network.umee.cc/bank/balances/umee12y0vrvpvh905femugdcc5z6ldd5js5rd0reuk2")
                .setHeaders(customHeaders)
                .build().execute();

        if (response.is200()) {
            System.out.println(response.getString());
            return response.getInstanceOf(Response.class);
        }
        return null;
    }

}

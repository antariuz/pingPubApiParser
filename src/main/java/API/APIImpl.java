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
    public Response getResponse(String walletAddress) throws Exception {
        if (walletAddress.contains("cosmos")) {
            AppHttpResponse response = AppHttpGetRequest.newBuilder()
                    .setUrl("https://api.flash.gaia-umeemania-1.network.umee.cc/bank/balances/" + walletAddress)
                    .setHeaders(customHeaders)
                    .build().execute();
            if (response.is200()) {
                return response.getInstanceOf(Response.class);
            }
        } else if (walletAddress.contains("umee")) {
            AppHttpResponse response = AppHttpGetRequest.newBuilder()
                    .setUrl("https://api.alley.umeemania-1.network.umee.cc/bank/balances/" + walletAddress)
                    .setHeaders(customHeaders)
                    .build().execute();
            if (response.is200()) {
                return response.getInstanceOf(Response.class);
            }
        } else if (walletAddress.contains("osmo")) {
            AppHttpResponse response = AppHttpGetRequest.newBuilder()
                    .setUrl("https://api.wall.osmosis-umeemania-1.network.umee.cc/bank/balances/" + walletAddress)
                    .setHeaders(customHeaders)
                    .build().execute();
            if (response.is200()) {
                return response.getInstanceOf(Response.class);
            }
        } else if (walletAddress.contains("juno")) {
            AppHttpResponse response = AppHttpGetRequest.newBuilder()
                    .setUrl("https://api.section.juno-umeemania-1.network.umee.cc/bank/balances/" + walletAddress)
                    .setHeaders(customHeaders)
                    .build().execute();
            if (response.is200()) {
                return response.getInstanceOf(Response.class);
            }
        } else if (walletAddress.contains("terra")) {
            AppHttpResponse response = AppHttpGetRequest.newBuilder()
                    .setUrl("https://api.street.terra-umeemania-1.network.umee.cc/bank/balances/" + walletAddress)
                    .setHeaders(customHeaders)
                    .build().execute();
            if (response.is200()) {
                return response.getInstanceOf(Response.class);
            }
        } else {
            throw new Exception("Wrong wallet address set");
        }
        return null;
    }

}

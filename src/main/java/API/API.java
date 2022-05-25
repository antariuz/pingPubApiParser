package API;

import model.Response;

public interface API {

    Response getResponse(String walletAddress) throws Exception;

}

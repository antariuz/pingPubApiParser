package API;

import model.apiModel.Response;

public interface API {

    Response getResponse(String walletAddress) throws Exception;

}

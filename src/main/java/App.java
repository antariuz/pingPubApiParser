import API.*;
import model.Response;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws Exception {

        API api = new APIImpl();

        Response response = api.getResponse();

        System.out.println(response);

    }
}

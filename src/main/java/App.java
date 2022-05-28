import model.Wallet;
import service.WalletAddressesService;

import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {

        List<Wallet> wallets = new WalletAddressesService().getTokensAmounts("src/main/resources/Z-server.txt");

        wallets.forEach(System.out::println);

    }
}

import model.Wallet;

import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {

        List<Wallet> wallets = new WalletAddresses().getTokensAmounts();
        wallets.forEach(System.out::println);

    }
}

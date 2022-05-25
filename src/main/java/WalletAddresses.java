import API.API;
import API.APIImpl;
import model.Chain;
import model.Result;
import model.SubChain;
import model.Wallet;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class WalletAddresses {

    private final File namesFile = new File("src/main/resources/namesMy.txt");
    private final File addressesFile = new File("src/main/resources/addressesMy.txt");
    private final API api = new APIImpl();
    private final Integer U = 1_000_000;

    public List<Wallet> init() throws Exception {
        List<Wallet> wallets = new ArrayList<>();
        List<String> names = Files.readAllLines(namesFile.toPath());
        for (String name : names) {
            Wallet wallet = new Wallet();
            wallet.setName(name);
            wallets.add(wallet);
        }
        int i = 0;
        List<String> allLines = Files.readAllLines(addressesFile.toPath());
        for (String line : allLines) {
            String[] addressesList = line.split(";");
            List<Chain> chains = new ArrayList<>();
            for (String address : addressesList) {
                Chain chain = new Chain();
                if (address.contains("cosmos")) {
                    chain.setAddress(address);
                } else if (address.contains("umee")) {
                    chain.setAddress(address);
                } else if (address.contains("osmo")) {
                    chain.setAddress(address);
                } else if (address.contains("juno")) {
                    chain.setAddress(address);
                } else {
                    throw new Exception("Wrong Chain set");
                }
                chains.add(chain);
            }
            Wallet wallet = wallets.get(i);
            wallet.setChains(chains);
            i++;
        }
        return wallets;
    }

    public List<Wallet> getTokensAmounts() throws Exception {
        List<Wallet> wallets = init();
        for (Wallet wallet : wallets) {
            for (Chain chain : wallet.getChains()) {
                List<SubChain> subChainList = new ArrayList<>();
                for (Result result : api.getResponse(chain.getAddress()).getResult()) {
                    SubChain subChain = new SubChain();
                    if (result.getDenom().equals("uatom")) {
                        subChain.setDenom("ATOM");
                    } else if (result.getDenom().equals("uumee")) {
                        subChain.setDenom("UMEE");
                    } else if (result.getDenom().equals("uosmo") |
                            result.getDenom().equals("ibc/646315E3B0461F5FA4C5C8968A88FC45D4D5D04A45B98F1B8294DD82F386DD85")) {
                        subChain.setDenom("OSMO");
                    } else if (result.getDenom().equals("ujuno") |
                            result.getDenom().equals("ibc/C814F0B662234E24248AE3B2FE2C1B54BBAF12934B757F6E7BC5AEC119963895")) {
                        subChain.setDenom("JUNO");
                    } else {
                        throw new Exception("Unknown token name: " + result.getDenom());
                    }
                    subChain.setAmount(Double.parseDouble(result.getAmount()) / U);
                    subChainList.add(subChain);
                }
                chain.setSubChain(subChainList);
            }
        }
        return wallets;
    }

}

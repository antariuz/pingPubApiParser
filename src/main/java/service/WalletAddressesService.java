package service;

import API.API;
import API.APIImpl;
import model.Chain;
import model.apiModel.Result;
import model.SubChain;
import model.Wallet;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class WalletAddressesService {
    private final API api = new APIImpl();
    private final Integer U = 1_000_000;

    public List<Wallet> init(String filePath) throws Exception {
        List<Wallet> wallets = new ArrayList<>();
        List<String> allLines = Files.readAllLines(new File(filePath).toPath());
        for (String line : allLines) {
            Wallet wallet = new Wallet();
            List<Chain> chains = new ArrayList<>();
            String[] addressesList = line.split(";");
            for (int i = 0; i < addressesList.length; i++) {
                if (i == 0) {
                    wallet.setName(addressesList[i]);
                } else {
                    if (addressesList[i].contains("cosmos") |
                            addressesList[i].contains("umee") |
                            addressesList[i].contains("osmo") |
                            addressesList[i].contains("juno")) {
                        Chain chain = new Chain();
                        chain.setAddress(addressesList[i]);
                        chains.add(chain);
                    } else {
                        throw new Exception("Wrong wallet address. Please, check it: " + addressesList[i]);
                    }
                }
            }
            wallet.setChains(chains);
            wallets.add(wallet);
        }
        return wallets;
    }

    public List<Wallet> getTokensAmounts(String filePath) throws Exception {
        List<Wallet> wallets = init(filePath);
        for (Wallet wallet : wallets) {
            for (Chain chain : wallet.getChains()) {
                List<SubChain> subChainList = new ArrayList<>();
                for (Result result : api.getResponse(chain.getAddress()).getResult()) {
                    SubChain subChain = new SubChain();
                    if (result.getDenom().equals("uatom") |
                    result.getDenom().equals("ibc/27394FB092D2ECCD56123C74F36E4C1F926001CEADA9CA97EA622B25F41E5EB2")) {
                        subChain.setDenom("ATOM");
                    } else if (result.getDenom().equals("uumee")) {
                        subChain.setDenom("UMEE");
                    } else if (result.getDenom().equals("uosmo") |
                            result.getDenom().equals("ibc/646315E3B0461F5FA4C5C8968A88FC45D4D5D04A45B98F1B8294DD82F386DD85")) {
                        subChain.setDenom("OSMO");
                    } else if (result.getDenom().equals("ujuno") |
                            result.getDenom().equals("ibc/C814F0B662234E24248AE3B2FE2C1B54BBAF12934B757F6E7BC5AEC119963895")) {
                        subChain.setDenom("JUNO");
                    } else if (result.getDenom().equals("ibc/A1CA75737A469737878C6A3CCD0D47738E85CCE0C4C341B298928DE7DADE3CDF")) {
                        subChain.setDenom("LUNA");
                    } else if (result.getDenom().equals("ibc/B27E587A396CE3964ED0AE196E3BB22CA22C7EE21112F8F81F004313D24142D3")) {
                        subChain.setDenom("UST");
                    } else {
                        throw new Exception("Unknown token name: " + result.getDenom() + "\n" + "Wallet address: " + chain.getAddress());
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

package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {

    private String name;
    private List<Chain> chains;

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(name).append(";");
        for (Chain chain : chains) {
            str.append(chain.getAddress()).append(";");
            for (SubChain subChain : chain.getSubChain()) {
                str.append(subChain.getAmount()).append(" ").append(subChain.getDenom()).append(",");
            }
            str.append(";");
        }
        return str.toString();
    }
}

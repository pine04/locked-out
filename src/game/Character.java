package game;

import java.util.List;

public class Character {
    private String identifier;
    private ExchangeTable exchangeTable;

    public Character() {

    }

    public String talk() {
        return "";
    }

    public ExchangeResult trade(List<Item> items) {
        return null;
    }
}

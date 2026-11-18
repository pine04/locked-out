package game;

import java.util.List;

public class Character {
    private final String identifier;
    private final String name;
    private ExchangeTable exchangeTable;

    public Character(String identifier, String name) {
        this.identifier = identifier;
        this.name = name;
    }

    public String talk() {
        return "";
    }

    public ExchangeResult trade(List<Item> items) {
        return exchangeTable.exchange(items);
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }
}

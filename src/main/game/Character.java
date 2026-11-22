package game;

import java.util.List;

public class Character {
    private final String identifier;
    private final String name;
    private final String dialogue;
    private ExchangeTable exchangeTable;



    public Character(String identifier, String name,String dialogue) {
        this.identifier = identifier;
        this.name = name;
        this.dialogue=dialogue;
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

package game;

import java.util.List;

public class Fixture {
    private final String identifier;
    private final String name;
    private ExchangeTable exchangeTable;

    public Fixture(String identifier, String name) {
        this.identifier = identifier;
        this.name = name;
    }

    public ExchangeResult interact(List<Item> items) {
        return null;
    }
}

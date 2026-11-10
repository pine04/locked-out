package game;

import java.util.List;

public class ExchangeTable {
    private List<Item> tmpInventory;
    private List<ExchangeTableEntry> entries;

    public ExchangeTable() {

    }

    private boolean accepts(String itemIdentifier) {
        return true;
    }

    public ExchangeResult exchange(List<Item> items) {
        return null;
    }
}

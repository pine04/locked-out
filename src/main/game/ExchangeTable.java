package game;

import java.util.ArrayList;
import java.util.List;

public class ExchangeTable {
    private final List<Item> tmpInventory;
    private final List<ExchangeTableEntry> entries;

    private ExchangeTable(Builder builder) {
        tmpInventory = new ArrayList<>();
        entries = builder.entries;
    }

    private boolean accepts(Item item) {
        for (ExchangeTableEntry entry : entries) {
            if (entry.acceptsNewExchanges() && entry.requires(item.identifier())) {
                return true;
            }
        }
        return false;
    }

    public ExchangeResult exchange(List<Item> items) {
        List<Item> rejectedItems = new ArrayList<>();
        for (Item item : items) {
            if (accepts(item)) {
                tmpInventory.add(item);
            } else {
                rejectedItems.add(item);
            }
        }

        ExchangeResult finalResult = ExchangeResult.empty();
        finalResult.addRejectedItems(rejectedItems);

        if (!tmpInventory.isEmpty()) {
            for (ExchangeTableEntry entry : entries) {
                ExchangeResult result = entry.tryExchange(tmpInventory);
                finalResult.merge(result);
                tmpInventory.removeAll(result.getConsumedItems());
                tmpInventory.removeAll(result.getReturnedItems());
            }
            finalResult.addWithheldItems(tmpInventory);
        }

        return finalResult;
    }

    public static class Builder {
        private final List<ExchangeTableEntry> entries;

        public Builder() {
            entries = new ArrayList<>();
        }

        public Builder addEntry(ExchangeTableEntry entry) {
            entries.add(entry);
            return this;
        }

        public ExchangeTable build() {
            return new ExchangeTable(this);
        }
    }
}

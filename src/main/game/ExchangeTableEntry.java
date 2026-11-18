package game;

import java.util.*;
import java.util.function.Supplier;

public class ExchangeTableEntry {
    private final Map<String, ItemConsumption> inputItems;
    private final Supplier<List<Item>> reward;
    private final SideEffect sideEffect;
    private final int maxTimesRewarded;
    private int timesRewarded;

    private ExchangeTableEntry(Builder builder) {
        inputItems = builder.inputItems;
        reward = builder.reward;
        sideEffect = builder.sideEffect;
        timesRewarded = 0;
        maxTimesRewarded = builder.maxTimesRewarded;
    }

    public boolean acceptsNewExchanges() {
        return maxTimesRewarded < 0 || timesRewarded < maxTimesRewarded;
    }

    public ExchangeResult tryExchange(List<Item> items) {
        if (!acceptsNewExchanges()) {
            return ExchangeResult.empty();
        }

        List<Item> consumedItems = new ArrayList<>();
        List<Item> returnedItems = new ArrayList<>();
        for (String requiredItemId : inputItems.keySet()) {
            Optional<Item> item = items.stream().filter(i -> i.identifier().equals(requiredItemId)).findFirst();
            if (item.isEmpty()) {
                return ExchangeResult.empty();
            }
            if (inputItems.get(requiredItemId) == ItemConsumption.CONSUMED) {
                consumedItems.add(item.get());
            } else {
                if (inputItems.get(requiredItemId) == ItemConsumption.RETURNED) {
                    returnedItems.add(item.get());
                }
            }
        }

        List<Item> rewardedItems;
        if (reward != null) {
            rewardedItems = reward.get();
        } else {
            rewardedItems = new ArrayList<>();
        }

        if (sideEffect != null) {
            sideEffect.execute();
        }

        timesRewarded++;

        return new ExchangeResult.Builder().consumedItems(consumedItems)
                .returnedItems(returnedItems)
                .rewardedItems(rewardedItems)
                .build();
    }

    public boolean requires(String itemIdentifier) {
        return inputItems.containsKey(itemIdentifier);
    }

    public static class Builder {
        private final Map<String, ItemConsumption> inputItems;
        private Supplier<List<Item>> reward;
        private SideEffect sideEffect;
        private int maxTimesRewarded;

        public Builder() {
            inputItems = new HashMap<>();
            maxTimesRewarded = -1;
        }

        public Builder addItem(String itemIdentifier, ItemConsumption consumption) {
            inputItems.put(itemIdentifier, consumption);
            return this;
        }

        public Builder reward(Supplier<List<Item>> reward) {
            this.reward = reward;
            return this;
        }

        public Builder sideEffect(SideEffect sideEffect) {
            this.sideEffect = sideEffect;
            return this;
        }

        public Builder maxTimesRewarded(int maxTimesRewarded) {
            this.maxTimesRewarded = maxTimesRewarded;
            return this;
        }

        public ExchangeTableEntry build() {
            return new ExchangeTableEntry(this);
        }
    }
}

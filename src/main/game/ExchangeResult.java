package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class ExchangeResult {
    private List<Item> rejectedItems;
    private List<Item> consumedItems;
    private List<Item> returnedItems;
    private List<Item> rewardedItems;
    private List<Item> withheldItems;

    private ExchangeResult(Builder builder) {
        rejectedItems = builder.rejectedItems;
        consumedItems = builder.consumedItems;
        returnedItems = builder.returnedItems;
        rewardedItems = builder.rewardedItems;
        withheldItems = builder.withheldItems;
    }

    public static ExchangeResult empty() {
        return new ExchangeResult.Builder().build();
    }

    public void merge(ExchangeResult result) {
        rejectedItems = Stream.concat(rejectedItems.stream(), result.rejectedItems.stream()).toList();
        consumedItems = Stream.concat(consumedItems.stream(), result.consumedItems.stream()).toList();
        returnedItems = Stream.concat(returnedItems.stream(), result.returnedItems.stream()).toList();
        rewardedItems = Stream.concat(rewardedItems.stream(), result.rewardedItems.stream()).toList();
        withheldItems = Stream.concat(withheldItems.stream(), result.withheldItems.stream()).toList();
    }

    public List<Item> getRejectedItems() {
        return Collections.unmodifiableList(rejectedItems);
    }

    public List<Item> getConsumedItems() {
        return Collections.unmodifiableList(consumedItems);
    }

    public List<Item> getReturnedItems() {
        return Collections.unmodifiableList(returnedItems);
    }

    public List<Item> getRewardedItems() {
        return Collections.unmodifiableList(rewardedItems);
    }

    public List<Item> getWithheldItems() {
        return Collections.unmodifiableList(withheldItems);
    }

    public void addRejectedItems(List<Item> rejectedItems) {
        this.rejectedItems = Stream.concat(this.rejectedItems.stream(), rejectedItems.stream()).toList();
    }

    public void addConsumedItems(List<Item> consumedItems) {
        this.consumedItems = Stream.concat(this.consumedItems.stream(), consumedItems.stream()).toList();
    }

    public void addReturnedItems(List<Item> returnedItems) {
        this.returnedItems = Stream.concat(this.returnedItems.stream(), returnedItems.stream()).toList();
    }

    public void addRewardedItems(List<Item> rewardedItems) {
        this.rewardedItems = Stream.concat(this.rewardedItems.stream(), rewardedItems.stream()).toList();
    }

    public void addWithheldItems(List<Item> withheldItems) {
        this.withheldItems = Stream.concat(this.withheldItems.stream(), withheldItems.stream()).toList();
    }

    public static class Builder {
        private List<Item> rejectedItems;
        private List<Item> consumedItems;
        private List<Item> returnedItems;
        private List<Item> rewardedItems;
        private List<Item> withheldItems;

        public Builder() {
            rejectedItems = new ArrayList<>();
            consumedItems = new ArrayList<>();
            returnedItems = new ArrayList<>();
            rewardedItems = new ArrayList<>();
            withheldItems = new ArrayList<>();
        }

        public Builder rejectedItems(List<Item> rejectedItems) {
            this.rejectedItems = rejectedItems;
            return this;
        }

        public Builder consumedItems(List<Item> consumedItems) {
            this.consumedItems = consumedItems;
            return this;
        }

        public Builder returnedItems(List<Item> returnedItems) {
            this.returnedItems = returnedItems;
            return this;
        }

        public Builder rewardedItems(List<Item> rewardedItems) {
            this.rewardedItems = rewardedItems;
            return this;
        }

        public Builder withheldItems(List<Item> withheldItems) {
            this.withheldItems = withheldItems;
            return this;
        }

        public ExchangeResult build() {
            return new ExchangeResult(this);
        }
    }
}

package game;

import java.util.List;

public record ExchangeResult(List<Item> rejectedItems, List<Item> consumedItems, List<Item> rewardedItems) {
}

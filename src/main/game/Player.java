package game;

import utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Player {
    private final Inventory inventory;
    private final Location location;

    public Player(Location startLocation, Inventory inventory) {
        this.inventory = inventory;
        location = startLocation;
    }

    public String look() {
        return "";
    }

    public String pickUp(String identifier) {
        return "";
    }

    public String drop(String identifier) {
        return "";
    }

    public String showInventory() {
        return "";
    }

    public String talkTo(String characterIdentifier) {
        return "";
    }

    public String give(String characterIdentifier, List<String> itemIdentifiers) {
        Optional<Character> characterOpt = location.getCharacter(characterIdentifier);
        if (characterOpt.isEmpty()) {
            return String.format("There is no character named %s in this location.", characterIdentifier);
        }

        List<Item> items = inventory.getMany(itemIdentifiers);
        if (items.size() < itemIdentifiers.size()) {
            List<String> returnedItemIds = items.stream().map(Item::identifier).toList();
            List<String> nonExistentIds = itemIdentifiers.stream().filter(id -> !returnedItemIds.contains(id)).toList();
            return String.format("You don't have these items in your inventory: %s.",
                                 StringUtils.joinWithAnd(nonExistentIds));
        }

        Character character = characterOpt.get();
        ExchangeResult giveResult = character.trade(items);
        for (Item consumedItem : giveResult.getConsumedItems()) {
            inventory.remove(consumedItem.identifier());
        }
        for (Item withheldItem : giveResult.getWithheldItems()) {
            inventory.remove(withheldItem.identifier());
        }
        for (Item rewardedItem : giveResult.getRewardedItems()) {
            boolean added = inventory.add(rewardedItem);
            if (!added) {
                location.leaveItem(rewardedItem);
            }
        }

        List<String> messages = new ArrayList<>();
        if (!giveResult.getRejectedItems().isEmpty()) {
            List<String> itemNames = giveResult.getRejectedItems().stream().map(Item::identifier).toList();
            messages.add(String.format("%s doesn't seem interested in %s.", character.getName(),
                                       StringUtils.joinWithAnd(itemNames)));
        }
        if (!giveResult.getReturnedItems().isEmpty()) {
            List<String> itemNames = giveResult.getReturnedItems().stream().map(Item::identifier).toList();
            messages.add(String.format("%s feels thankful for your %s and has given them back.", character.getName(),
                                       StringUtils.joinWithAnd(itemNames)));
        }
        if (!giveResult.getRewardedItems().isEmpty()) {
            List<String> itemNames = giveResult.getRewardedItems().stream().map(Item::identifier).toList();
            messages.add(String.format("%s gives you %s in return.", character.getName(),
                                       StringUtils.joinWithAnd(itemNames)));
        }
        if (!giveResult.getWithheldItems().isEmpty()) {
            List<String> itemNames = giveResult.getWithheldItems().stream().map(Item::identifier).toList();
            messages.add(
                    String.format("%s accepts your %s but needs something else to go with them.", character.getName(),
                                  StringUtils.joinWithAnd(itemNames)));
        }
        return String.join("\n\n", messages);
    }

    public String useOn(String fixtureIdentifier, List<String> itemIdentifiers) {
        return "";
    }

    public String moveTo(String locationIdentifier) {
        return "";
    }

    public String unlockExitTo(String locationIdentifier, String keyIdentifier) {
        return "";
    }
}

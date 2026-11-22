package game;

import utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Player {
    private final Inventory inventory;
    private Location location;

    public Player(Location startLocation, Inventory inventory) {
        this.inventory = inventory;
        location = startLocation;
    }


    public String look() {
        if (location == null) return "You are nowhere.";
        return location.describe();
    }

    public String pickUp(String identifier) {
        Optional<Item> itemOpt = location.getItem(identifier);
        if (itemOpt.isEmpty()) {
            return "There is no '" + identifier + "' here.";
        }
        Item item = itemOpt.get();
        if (inventory.add(item)) {
            location.removeItem(item);
            return "You picked up the " + item.identifier() + ".";
        } else {
            return "Your inventory is full!";
        }
    }
    public String drop(String identifier) {

        Optional<Item> itemOpt = inventory.get(identifier);
        if (itemOpt.isEmpty()) {
            return "You don't have a '" + identifier + "' in your inventory.";
        }
        Item item = itemOpt.get();
        inventory.remove(identifier);
        location.leaveItem(item);
        return "You dropped the " + item.identifier() + ".";
    }
    public String showInventory()
    {
        return inventory.listItemsAsString();
    }

    public String talkTo(String characterIdentifier) {
        Optional<Character> charOpt = location.getCharacter(characterIdentifier);
        if (charOpt.isEmpty()) {
            return "There is no " + characterIdentifier + " here.";
        }
        return charOpt.get().talk();
    }

    public String give(String characterIdentifier, List<String> itemIdentifiers) {
        Optional<Character> characterOpt = location.getCharacter(characterIdentifier);
        if (characterOpt.isEmpty()) {
            return String.format("There is no character named %s in this location.", characterIdentifier);
        }

        List<Item> items = inventory.getMany(itemIdentifiers);
        if (items.size() < itemIdentifiers.size()) {
            return buildMissingItemsMessage(itemIdentifiers, items);
        }

        Character character = characterOpt.get();
        ExchangeResult giveResult = character.trade(items);
        List<Item> abandonedItems = handleExchangeResult(giveResult);

        return buildGiveMessage(character, giveResult, abandonedItems);
    }

    public String useOn(String fixtureIdentifier, List<String> itemIdentifiers) {
        Optional<Fixture> fixtureOpt = location.getFixture(fixtureIdentifier);
        if (fixtureOpt.isEmpty()) {
            return String.format("There is no %s in this location.", fixtureIdentifier);
        }

        List<Item> items = inventory.getMany(itemIdentifiers);
        if (items.size() < itemIdentifiers.size()) {
            return buildMissingItemsMessage(itemIdentifiers, items);
        }

        Fixture fixture = fixtureOpt.get();
        ExchangeResult giveResult = fixture.interact(items);
        List<Item> abandonedItems = handleExchangeResult(giveResult);

        return buildUseOnMessage(fixture, giveResult, abandonedItems);
    }

    public String moveTo(String locationIdentifier) {
        GetDestinationResult destinationResult = location.getLocation(locationIdentifier);
        switch (destinationResult.status()) {
            case SUCCESS:
                location = destinationResult.location().orElse(location);
                return String.format("Moved to %s.", locationIdentifier);
            case EXIT_LOCKED:
                return String.format("The exit to %s is currently locked. You can open it with %s.", locationIdentifier, destinationResult.requiredKey());
            case NONEXISTENT_DESTINATION:
                return String.format("You can't go to %s from here.", locationIdentifier);
        }
        return "";
    }

    public String unlockExitTo(String locationIdentifier, String keyIdentifier) {
        Optional<Item> keyOpt = inventory.get(keyIdentifier);
        if (keyOpt.isEmpty()) {
            return String.format("You don't have %s in your inventory.", keyIdentifier);
        }
        UnlockExitStatus status = location.unlockExit(locationIdentifier, keyOpt.get());
        switch (status) {
            case SUCCESS:
                inventory.remove(keyIdentifier);
                return String.format("Unlocked %s with %s.", locationIdentifier, keyIdentifier);
            case ALREADY_UNLOCKED:
                return String.format("%s already unlocked.", StringUtils.capitalize(locationIdentifier));
            case INCORRECT_KEY:
                return String.format("You can't use this key to unlock %s.", locationIdentifier);
            case NONEXISTENT_DESTINATION:
                return String.format("You can't go to %s from here.", locationIdentifier);
        }
        return "";
    }

    private List<Item> handleExchangeResult(ExchangeResult exchangeResult) {
        for (Item consumedItem : exchangeResult.getConsumedItems()) {
            inventory.remove(consumedItem.identifier());
        }

        for (Item withheldItem : exchangeResult.getWithheldItems()) {
            inventory.remove(withheldItem.identifier());
        }

        List<Item> abandonedItems = new ArrayList<>();
        for (Item rewardedItem : exchangeResult.getRewardedItems()) {
            boolean added = inventory.add(rewardedItem);
            if (!added) {
                location.leaveItem(rewardedItem);
                abandonedItems.add(rewardedItem);
            }
        }

        return abandonedItems;
    }

    private String buildMissingItemsMessage(List<String> itemIdentifiers, List<Item> actualItems) {
        List<String> actualItemIds = actualItems.stream().map(Item::identifier).toList();
        List<String> nonExistentIds = itemIdentifiers.stream().filter(id -> !actualItemIds.contains(id)).toList();
        return String.format("You don't have these items in your inventory: %s.",
                             StringUtils.joinWithAnd(nonExistentIds));
    }

    private String buildGiveMessage(Character character, ExchangeResult exchangeResult, List<Item> abandonedItems) {
        List<String> messages = new ArrayList<>();
        if (!exchangeResult.getRejectedItems().isEmpty()) {
            List<String> itemNames = exchangeResult.getRejectedItems().stream().map(Item::identifier).toList();
            messages.add(String.format("%s doesn't seem interested in %s.", character.getName(),
                                       StringUtils.joinWithAnd(itemNames)));
        }
        if (!exchangeResult.getReturnedItems().isEmpty()) {
            List<String> itemNames = exchangeResult.getReturnedItems().stream().map(Item::identifier).toList();
            messages.add(String.format("%s feels thankful for your %s and has given them back.", character.getName(),
                                       StringUtils.joinWithAnd(itemNames)));
        }
        if (!exchangeResult.getRewardedItems().isEmpty()) {
            List<String> itemNames = exchangeResult.getRewardedItems().stream().map(Item::identifier).toList();
            messages.add(String.format("%s gives you %s in return.", character.getName(),
                                       StringUtils.joinWithAnd(itemNames)));
        }
        if (!abandonedItems.isEmpty()) {
            messages.add(buildAbandonedItemsMessage(abandonedItems));
        }
        if (!exchangeResult.getWithheldItems().isEmpty()) {
            List<String> itemNames = exchangeResult.getWithheldItems().stream().map(Item::identifier).toList();
            messages.add(
                    String.format("%s accepts your %s but needs something else to go with them.", character.getName(),
                                  StringUtils.joinWithAnd(itemNames)));
        }
        return String.join("\n\n", messages);
    }

    private String buildUseOnMessage(Fixture fixture, ExchangeResult exchangeResult, List<Item> abandonedItems) {
        List<String> messages = new ArrayList<>();
        if (!exchangeResult.getRejectedItems().isEmpty()) {
            List<String> itemNames = exchangeResult.getRejectedItems().stream().map(Item::identifier).toList();
            messages.add(
                    String.format("You can't use %s on %s.", StringUtils.joinWithAnd(itemNames), fixture.getName()));
        }
        if (!exchangeResult.getReturnedItems().isEmpty()) {
            List<String> itemNames = exchangeResult.getReturnedItems().stream().map(Item::identifier).toList();
            messages.add(String.format("You can still keep your %s after using them on %s.",
                                       StringUtils.joinWithAnd(itemNames), fixture.getName()));
        }
        if (!exchangeResult.getRewardedItems().isEmpty()) {
            List<String> itemNames = exchangeResult.getRewardedItems().stream().map(Item::identifier).toList();
            messages.add(String.format("You receive %s from %s as a reward.", StringUtils.joinWithAnd(itemNames),
                                       fixture.getName()));
        }
        if (!abandonedItems.isEmpty()) {
            messages.add(buildAbandonedItemsMessage(abandonedItems));
        }
        if (!exchangeResult.getWithheldItems().isEmpty()) {
            List<String> itemNames = exchangeResult.getWithheldItems().stream().map(Item::identifier).toList();
            messages.add(String.format("You use %s on %s, but perhaps something else is also needed.",
                                       StringUtils.joinWithAnd(itemNames), fixture.getName()));
        }
        return String.join("\n\n", messages);
    }

    private String buildAbandonedItemsMessage(List<Item> items) {
        List<String> itemNames = items.stream().map(Item::identifier).toList();
        return String.format(
                "However, as your inventory is full, you had to leave %s at your location. You need to take some items off your inventory to pick them up.",
                StringUtils.joinWithAnd(itemNames));
    }
}

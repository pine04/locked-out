package game;

import java.util.List;

public class Player {
    private Inventory inventory;
    private Location location;

    public Player(Location startLocation) {

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
        return "";
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

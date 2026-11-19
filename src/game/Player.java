
//Just for the player-lookcommand code start for : Look only


package game;

import java.util.List;

public class Player {
    private Inventory inventory;
    private Location location;

    public Player(Location startLocation) {
        this.location = startLocation;
        this.inventory = new Inventory(15);
    }

    public String look() {
        if (location == null) {
            return "You are nowhere. (Error: No location set)";
        }
        return location.describe();
    }

// ____end here____ and i dont distrub the other

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

    public String useOn(String itemIdentifier, String fixtureIdentifier) {
        return "";
    }

    public String moveTo(String locationIdentifier) {
        return "";
    }

    public String unlockExitTo(String locationIdentifier, String keyIdentifier) {
        return "";
    }
}

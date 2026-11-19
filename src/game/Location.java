package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Location {
    private final String identifier;
    private final String description;

    // Initialized the lists to prevent NullPointerExceptions!

    //items;
    private List<Item> items= new ArrayList<>();
    //characters;
    private List<Character>characters= new ArrayList<>();
    // fixtures
    private List<Fixture>fixtures = new ArrayList<>();
    //exits
    private List<Exit> exits = new ArrayList<>();

    public Location(String identifier, String description) {
        this.identifier = identifier;
        this.description = description;
    }

    //descibe class stated here please check if u change into it:
    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append(description).append("\n");

        // Show the items
        if (!items.isEmpty()) {
            sb.append("You see the items: ");
            for (Item item : items) {
                sb.append(item.identifier()).append(", ");
            }
            sb.append("\n");
        }

        // Shows the characters
        if (!characters.isEmpty()) {
            sb.append("People here: ");
            for (Character c : characters) {
                sb.append(c.getName()).append(", ");
            }
            sb.append("\n");
        }

        // Show fixtures
        if (!fixtures.isEmpty()) {
            sb.append("You notice: ");
            for (Fixture f : fixtures) {
                sb.append(f.getName()).append(", ");
            }
            sb.append("\n");
        }

        // Edge case: Room is completely empty
        if (items.isEmpty() && characters.isEmpty() && fixtures.isEmpty()) {
            sb.append("There is nothing else here.\n");
        }

        return sb.toString();
    }


    //issue related to accesss modifier (iterm) :
    // Line 19 and 20 we declared iterm as private in location-class and game.java try to called them from outside for grabing iterms.
    // This is a public method that allows other classes to safely add an item.
    public void addItem(Item item) {
        this.items.add(item);
    }

    //____end__



    public Optional<Item> getItem(String identifier) {
        return Optional.empty();
    }

    public boolean leaveItem(Item item) {
        return true;
    }

    public Optional<Fixture> getFixture(String identifier) {
        return Optional.empty();
    }

    public Optional<Character> getCharacter(String identifier) {
        return Optional.empty();
    }

    public GetDestinationResult getLocation(String identifier) {
        return null;
    }

    public UnlockExitStatus unlockExit(String locationIdentifier, String keyIdentifier) {
        return UnlockExitStatus.SUCCESS;
    }
}

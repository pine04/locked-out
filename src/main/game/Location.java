package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Location {
    private final String identifier;
    private final String description;
    private final List<Item> items = new ArrayList<>();
    private final List<Character> characters = new ArrayList<>();
    private final List<Fixture> fixtures = new ArrayList<>();
    private final List<Exit> exits = new ArrayList<>();

    public Location(String identifier, String description) {
        this.identifier = identifier;
        this.description = description;
    }


    public void leaveItem(Item item) {
        items.add(item);
    }

    public Optional<Fixture> getFixture(String identifier) {
        return fixtures.stream()
                .filter(fixture -> fixture.getIdentifier().equals(identifier))
                .findFirst();
    }

    public Optional<Character> getCharacter(String identifier) {
        return characters.stream()
                .filter(character -> character.getIdentifier().equals(identifier))
                .findFirst();
    }

    public GetDestinationResult getLocation(String identifier) {
        Optional<Exit> exitOpt = exits.stream().filter(exit -> exit.leadsTo(identifier)).findFirst();
        if (exitOpt.isEmpty()) {
            return new GetDestinationResult(GetDestinationStatus.NONEXISTENT_DESTINATION, Optional.empty(), "");
        }

        Exit exit = exitOpt.get();
        Optional<Location> destinationLocation = exit.getLocationIfUnlocked();
        if (destinationLocation.isEmpty()) {
            return new GetDestinationResult(GetDestinationStatus.EXIT_LOCKED, destinationLocation, exit.getAcceptedKey());
        }
        return new GetDestinationResult(GetDestinationStatus.SUCCESS, destinationLocation, "");
    }

    public UnlockExitStatus unlockExit(String locationIdentifier, Item key) {
        Optional<Exit> exitOpt = exits.stream().filter(exit -> exit.leadsTo(locationIdentifier)).findFirst();
        if (exitOpt.isEmpty()) {
            return UnlockExitStatus.NONEXISTENT_DESTINATION;
        }

        return exitOpt.get().unlock(key);
    }

    public String getIdentifier() {
        return identifier;
    }

    public static void connect(Location origin, Location destination) {
        origin.exits.add(new Exit(destination));
        destination.exits.add(new Exit(origin));
    }

    public static void connect(Location origin, Location destination, String acceptedKey) {
        origin.exits.add(new Exit(destination, acceptedKey));
        destination.exits.add(new Exit(origin, acceptedKey));
    }
    // abdullah this side now i added these :
    public void addItem(Item item) {
        this.items.add(item);
    }

    public java.util.Optional<Item> getItem(String identifier) {
        for (Item item : items) {
            if (item.identifier().equalsIgnoreCase(identifier)) return java.util.Optional.of(item);
        }
        return java.util.Optional.empty();
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append(description).append("\n");
        if (!items.isEmpty()) {
            sb.append("You see items: ");
            for (Item item : items) sb.append(item.identifier()).append(", ");
            sb.append("\n");
        }
        if (!characters.isEmpty()) {
            sb.append("People here: ");
            for (Character c : characters) sb.append(c.getName()).append(", ");
            sb.append("\n");
        }
        if (!fixtures.isEmpty()) {
            sb.append("You notice: ");
            for (Fixture f : fixtures) sb.append(f.getName()).append(", ");
            sb.append("\n");
        }
        if (items.isEmpty() && characters.isEmpty() && fixtures.isEmpty()) {
            sb.append("There is nothing else here.\n");
        }
        return sb.toString();
    }
}

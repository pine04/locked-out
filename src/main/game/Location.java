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

    public String describe() {
        return "";
    }

    public Optional<Item> getItem(String identifier) {
        return Optional.empty();
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
}

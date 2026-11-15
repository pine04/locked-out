package game;

import java.util.List;
import java.util.Optional;

public class Location {
    private final String identifier;
    private final String description;
    private List<Item> items;
    private List<Character> characters;
    private List<Fixture> fixtures;
    private List<Exit> exits;

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

    public boolean leaveItem(Item item) {
        return true;
    }

    public Optional<Fixture> getFixture(String identifier) {
        return Optional.empty();
    }

    public Optional<Character> getCharacter(String identifier) {
        return characters.stream()
                .filter(character -> character.getIdentifier().equals(identifier))
                .findFirst();
    }

    public GetDestinationResult getLocation(String identifier) {
        return null;
    }

    public UnlockExitStatus unlockExit(String locationIdentifier, String keyIdentifier) {
        return UnlockExitStatus.SUCCESS;
    }
}

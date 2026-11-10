package game;

import java.util.List;
import java.util.Optional;

public class Location {
    private String identifier;
    private String description;
    private List<Item> items;
    private List<Character> characters;
    private List<Fixture> fixtures;
    private List<Exit> exits;

    public Location() {

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
        return Optional.empty();
    }

    public GetLocationResult getLocation(String identifier) {
        return null;
    }

    public boolean unlockExit(String locationIdentifier, String keyIdentifier) {
        return true;
    }
}

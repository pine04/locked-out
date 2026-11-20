package game;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList; // (abdullah) added this import Added = new ArrayList<>()

public class Inventory {
    private final int size;
    private List<Item> items = new ArrayList<>(); // < Added = new ArrayList<>()

    public Inventory(int size) {
        this.size = size;
    }

    public String listItemsAsString() {
        if (items.isEmpty()) return "Your inventory is empty.";
        StringBuilder sb = new StringBuilder("Inventory:\n");
        for (Item item : items) {
            sb.append("- ").append(item.identifier()).append("\n");
        }
        return sb.toString();
    }

    public boolean has(String identifier) {
        return true; // We can fix this later
    }

    public Optional<Item> get(String identifier) {
        return items.stream().filter(item -> item.identifier().equals(identifier)).findFirst();
    }

    public List<Item> getMany(List<String> identifiers) {
        return List.of();
    }

    //Used by PickUpCommand
    public boolean add(Item item) {
        if (items.size() >= size) {
            return false; // Inventory is full
        }
        items.add(item);
        return true;
    }

    public boolean remove(String identifier) {
        return items.removeIf(item -> item.identifier().equalsIgnoreCase(identifier));
    }
}
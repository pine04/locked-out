package game;

import java.util.List;

public class Inventory {
    private final int size;
    private List<Item> items;

    public Inventory(int size) {
        this.size = size;
    }

    public String listItemsAsString() {
        return "";
    }

    public boolean has(String identifier) {
        return true;
    }

    public List<Item> getMany(List<String> identifiers) {
        return List.of();
    }

    public boolean add(Item item) {
        return true;
    }

    public boolean remove(String identifier) {
        return true;
    }
}

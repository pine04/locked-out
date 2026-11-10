package game;

import java.util.List;

public class Inventory {
    private int size;
    private List<Item> items;

    public Inventory(int size) {

    }

    public String listItemsAsString() {
        return "";
    }

    public boolean has(String identifier) {
        return true;
    }

    public boolean add(Item item) {
        return true;
    }

    public boolean remove(String identifier) {
        return true;
    }
}

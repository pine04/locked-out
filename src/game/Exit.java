package game;

public class Exit {
    private Location destination;
    private boolean isUnlocked;
    private String acceptedKey;

    public Exit() {

    }

    public boolean unlock(String keyIdentifier) {
        return true;
    }

    public Location getLocation() {
        return null;
    }
}

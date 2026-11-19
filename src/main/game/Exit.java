package game;

import java.util.Optional;

public class Exit {
    private final Location destination;
    private boolean isUnlocked;
    private final String acceptedKey;

    public Exit(Location destination) {
        this.destination = destination;
        isUnlocked = true;
        acceptedKey = "";
    }

    public Exit(Location destination, String acceptedKey) {
        this.destination = destination;
        isUnlocked = false;
        this.acceptedKey = acceptedKey;
    }

    public boolean leadsTo(String destinationIdentifier) {
        return destination.getIdentifier().equals(destinationIdentifier);
    }

    public UnlockExitStatus unlock(Item key) {
        if (isUnlocked) {
            return UnlockExitStatus.ALREADY_UNLOCKED;
        } else if (acceptedKey.equals(key.identifier())) {
            isUnlocked = true;
            return UnlockExitStatus.SUCCESS;
        } else {
            return UnlockExitStatus.INCORRECT_KEY;
        }
    }

    public Optional<Location> getLocationIfUnlocked() {
        if (isUnlocked) {
            return Optional.of(destination);
        }
        return Optional.empty();
    }

    public String getAcceptedKey() {
        return acceptedKey;
    }
}

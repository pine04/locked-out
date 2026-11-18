package game;

import java.util.Optional;

public class Exit {
    private Location destination;
    private boolean isUnlocked;
    private String acceptedKey;

    public Exit() {

    }

    public UnlockExitStatus unlock(String keyIdentifier) {
        return UnlockExitStatus.SUCCESS;
    }

    public Optional<Location> getLocationIfUnlocked() {
        return Optional.empty();
    }
}

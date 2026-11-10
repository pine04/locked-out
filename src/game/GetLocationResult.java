package game;

import java.util.Optional;

public record GetLocationResult(GetLocationStatus status, Optional<Location> location) {
}

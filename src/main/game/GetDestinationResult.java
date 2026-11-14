package game;

import java.util.Optional;

public record GetDestinationResult(GetDestinationStatus status, Optional<Location> location) {
}

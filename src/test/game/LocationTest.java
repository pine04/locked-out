package game;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LocationTest {
    private Location room;
    private Location hallway;

    @Before
    public void setUp() throws Exception {
        room = new Location("room", "my room");
        hallway = new Location("hallway", "hallway next to room");
    }

    @Test
    public void givenExistingUnlockedDestination_whenGetLocation_shouldReturnResultWithDestination() {
        Location.connect(room, hallway);

        GetDestinationResult result = room.getLocation("hallway");

        assertEquals(GetDestinationStatus.SUCCESS, result.status());
        assertEquals(hallway, result.location().orElse(null));
    }

    @Test
    public void givenExistingLockedDestination_whenGetLocation_shouldReturnResultWithNoDestination() {
        Location.connect(room, hallway, "hallway key");

        GetDestinationResult result = room.getLocation("hallway");

        assertEquals(GetDestinationStatus.EXIT_LOCKED, result.status());
        assertTrue(result.location().isEmpty());
    }

    @Test
    public void givenNonexistentDestination_whenGetLocation_shouldReturnResultWithNoDestination() {
        GetDestinationResult result = room.getLocation("hallway");

        assertEquals(GetDestinationStatus.NONEXISTENT_DESTINATION, result.status());
        assertTrue(result.location().isEmpty());
    }
}
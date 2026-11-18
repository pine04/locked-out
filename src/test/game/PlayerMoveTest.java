package game;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlayerMoveTest {
    private Player player;
    @Mock
    private Location location;
    @Mock
    private Location destination;

    @Before
    public void setUp() throws Exception {
        player = new Player(location, new Inventory(5));
    }

    @Test
    public void givenIdForExistingAndUnlockedDestination_whenMoveTo_shouldMovePlayerToDestination() {
        GetDestinationResult destinationResult = new GetDestinationResult(GetDestinationStatus.SUCCESS, Optional.of(destination), "");
        when(location.getLocation("kitchen")).thenReturn(destinationResult);

        String result = player.moveTo("kitchen");

        assertEquals("Moved to kitchen.", result);
    }

    @Test
    public void givenIdForExistingButLockedDestination_whenMoveTo_shouldNotMovePlayerToDestination() {
        GetDestinationResult destinationResult = new GetDestinationResult(GetDestinationStatus.EXIT_LOCKED, Optional.empty(), "kitchen key");
        when(location.getLocation("kitchen")).thenReturn(destinationResult);

        String result = player.moveTo("kitchen");

        assertEquals("The exit to kitchen is currently locked. You can open it with kitchen key.", result);
    }

    @Test
    public void givenIdForNonexistentDestination_whenMoveTo_shouldReturnCorrectMessageToPlayer() {
        GetDestinationResult destinationResult = new GetDestinationResult(GetDestinationStatus.NONEXISTENT_DESTINATION, Optional.empty(), "");
        when(location.getLocation("kitchen")).thenReturn(destinationResult);

        String result = player.moveTo("kitchen");

        assertEquals("You can't go to kitchen from here.", result);
    }
}
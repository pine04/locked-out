package game;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExitTest {
    @Mock
    private Location location;
    @Mock
    private Item key;

    @Before
    public void setUp() {
        when(location.getIdentifier()).thenReturn("kitchen");
        when(key.identifier()).thenReturn("key");
    }

    @Test
    public void givenCorrectDestinationId_whenCallLeadsTo_shouldReturnTrue() {
        Exit exit = new Exit(location);

        assertTrue(exit.leadsTo("kitchen"));
    }

    @Test
    public void givenIncorrectDestinationId_whenCallLeadsTo_shouldReturnFalse() {
        Exit exit = new Exit(location);

        assertFalse(exit.leadsTo("hallway"));
    }

    @Test
    public void givenAlreadyUnlockedExit_whenCallUnlock_shouldReturnAlreadyUnlockedStatus() {
        Exit exit = new Exit(location);

        assertEquals(UnlockExitStatus.ALREADY_UNLOCKED, exit.unlock(key));
    }

    @Test
    public void givenLockedExit_whenCallUnlockWithCorrectKey_shouldUnlockAndReturnSuccessStatus() {
        Exit exit = new Exit(location, "key");

        UnlockExitStatus unlockStatus = exit.unlock(key);
        Optional<Location> locationOpt = exit.getLocationIfUnlocked();

        assertEquals(UnlockExitStatus.SUCCESS, unlockStatus);
        assertEquals(location, locationOpt.orElse(null));
    }

    @Test
    public void givenLockedExit_whenCallUnlockWithIncorrectKey_shouldReturnIncorrectKeyStatusAndNotUnlock() {
        Exit exit = new Exit(location, "key");
        when(key.identifier()).thenReturn("wrong key");

        UnlockExitStatus unlockStatus = exit.unlock(key);
        Optional<Location> locationOpt = exit.getLocationIfUnlocked();

        assertEquals(UnlockExitStatus.INCORRECT_KEY, unlockStatus);
        assertTrue(locationOpt.isEmpty());
    }

    @Test
    public void givenUnlockedExit_whenCallGetLocationIfUnlocked_shouldNotReturnDestination() {
        Exit exit = new Exit(location);

        Optional<Location> result = exit.getLocationIfUnlocked();

        assertEquals(location, result.orElse(null));
    }

    @Test
    public void givenLockedExit_whenCallGetLocationIfUnlocked_shouldNotReturnDestination() {
        Exit exit = new Exit(location, "key");

        Optional<Location> result = exit.getLocationIfUnlocked();

        assertTrue(result.isEmpty());
    }

    @Test
    public void givenLockedExit_whenCallUnlockWithCorrectKeyThenGetLocationIfUnlocked_shouldReturnDestination() {
        Exit exit = new Exit(location, "key");

        UnlockExitStatus unlockStatus = exit.unlock(key);
        Optional<Location> result = exit.getLocationIfUnlocked();

        assertEquals(location, result.orElse(null));
    }
}
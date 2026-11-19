package game;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PlayerUnlockTest {
    private Player player;
    @Mock
    private Location location;
    @Mock
    private Inventory inventory;
    @Mock
    private Item key;

    @Before
    public void setUp() throws Exception {
        player = new Player(location, inventory);
        when(inventory.get("key")).thenReturn(Optional.of(key));
    }

    @Test
    public void givenKeyNotInInventory_whenCallUnlockExitTo_shouldReturnMessageSayingKeyNotInInventory() {
        when(inventory.get("key")).thenReturn(Optional.empty());

        String result = player.unlockExitTo("kitchen", "key");

        assertEquals("You don't have key in your inventory.", result);
    }

    @Test
    public void givenKeyInInventoryAndSuccessfulUnlock_whenCallUnlockExitTo_shouldRemoveKeyFromInventoryAndReturnUnlockedMessage() {
        when(location.unlockExit("kitchen", key)).thenReturn(UnlockExitStatus.SUCCESS);

        String result = player.unlockExitTo("kitchen", "key");

        verify(inventory).remove("key");
        assertEquals("Unlocked kitchen with key.", result);
    }

    @Test
    public void givenKeyInInventoryAndAlreadyUnlockedExit_whenCallUnlockExitTo_shouldReturnAlreadyUnlockedMessageAndNotRemoveKey() {
        when(location.unlockExit("kitchen", key)).thenReturn(UnlockExitStatus.ALREADY_UNLOCKED);

        String result = player.unlockExitTo("kitchen", "key");

        verify(inventory, never()).remove("key");
        assertEquals("Kitchen already unlocked.", result);
    }

    @Test
    public void givenIncorrectKey_whenCallUnlockExitTo_shouldReturnIncorrectKeyMessage() {
        when(location.unlockExit("kitchen", key)).thenReturn(UnlockExitStatus.INCORRECT_KEY);

        String result = player.unlockExitTo("kitchen", "key");

        assertEquals("You can't use this key to unlock kitchen.", result);
    }

    @Test
    public void givenNonexistentDestination_whenCallUnlockExitTo_shouldReturnNonexistentDestinationMessage() {
        when(location.unlockExit("kitchen", key)).thenReturn(UnlockExitStatus.NONEXISTENT_DESTINATION);

        String result = player.unlockExitTo("kitchen", "key");

        assertEquals("You can't go to kitchen from here.", result);
    }
}
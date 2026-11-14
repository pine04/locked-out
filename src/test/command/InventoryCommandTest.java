package command;

import game.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InventoryCommandTest {
    @Mock
    private Player player;
    private InventoryCommand command;

    @Before
    public void setUp() throws Exception {
        command = new InventoryCommand(player);
    }

    @Test
    public void givenCorrectCommand_whenParseAndExecute_shouldExecute() {
        CommandParseStatus status = command.tryParse("inventory");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).showInventory();
    }

    @Test
    public void givenCorrectCapitalizedCommand_whenParseAndExecute_shouldExecute() {
        CommandParseStatus status = command.tryParse("inVenTORy");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).showInventory();
    }

    @Test
    public void givenWrongSyntaxCommand_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("inventory list items");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenCorrectCommandWithSpaces_whenParseAndExecute_shouldExecute() {
        CommandParseStatus status = command.tryParse("   inventory        ");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).showInventory();
    }

    @Test
    public void givenIncorrectCommandStartingWithInventory_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("inventoryasdasdasd");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenUnknownCommand_whenParse_shouldReturnNotRecognizedStatus() {
        CommandParseStatus status = command.tryParse("check inventory");

        assertEquals(CommandParseStatus.NOT_RECOGNIZED, status);
    }
}
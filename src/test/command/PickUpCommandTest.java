package command;

import game.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PickUpCommandTest {
    @Mock
    private Player player;
    private PickUpCommand command;

    @Before
    public void setUp() throws Exception {
        command = new PickUpCommand(player);
    }

    @Test
    public void givenCorrectCommand_whenParseAndExecute_shouldExecuteWithCorrectParameters() {
        CommandParseStatus status = command.tryParse("pick up ring");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).pickUp("ring");
    }

    @Test
    public void givenCorrectCapitalizedCommand_whenParseAndExecute_shouldExecuteWithCorrectParameters() {
        CommandParseStatus status = command.tryParse("PIcK up rINg");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).pickUp("ring");
    }

    @Test
    public void givenWrongSyntaxCommand_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("pick upp ring");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenCommandWithNoItemArgument_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("pick up");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenCommandWithEmptyItemIds_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("pick up     ");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenCorrectCommandWithSpaces_whenParseAndExecute_shouldExecuteWithCorrectlyTrimmedParameters() {
        CommandParseStatus status = command.tryParse("   pick    up     ring        ");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).pickUp("ring");
    }

    @Test
    public void givenIncorrectCommandStartingWithPickUp_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("pick upasdasdasd");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenUnknownCommand_whenParse_shouldReturnNotRecognizedStatus() {
        CommandParseStatus status = command.tryParse("grab ring");

        assertEquals(CommandParseStatus.NOT_RECOGNIZED, status);
    }
}
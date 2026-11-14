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
public class DropCommandTest {
    @Mock
    private Player player;
    private DropCommand command;

    @Before
    public void setUp() throws Exception {
        command = new DropCommand(player);
    }

    @Test
    public void givenCorrectCommand_whenParseAndExecute_shouldExecuteWithCorrectParameters() {
        CommandParseStatus status = command.tryParse("drop ring");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).drop("ring");
    }

    @Test
    public void givenCorrectCapitalizedCommand_whenParseAndExecute_shouldExecuteWithCorrectParameters() {
        CommandParseStatus status = command.tryParse("drOP rINg");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).drop("ring");
    }

    @Test
    public void givenWrongSyntaxCommand_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("dropp ring");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenCommandWithNoItemArgument_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("drop");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenCommandWithEmptyItemIds_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("drop     ");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenCorrectCommandWithSpaces_whenParseAndExecute_shouldExecuteWithCorrectlyTrimmedParameters() {
        CommandParseStatus status = command.tryParse("   drop     ring        ");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).drop("ring");
    }

    @Test
    public void givenIncorrectCommandStartingWithPickUp_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("dropupasdasdasd");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenUnknownCommand_whenParse_shouldReturnNotRecognizedStatus() {
        CommandParseStatus status = command.tryParse("leave behind ring");

        assertEquals(CommandParseStatus.NOT_RECOGNIZED, status);
    }
}
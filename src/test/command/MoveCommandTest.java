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
public class MoveCommandTest {
    @Mock
    private Player player;
    private MoveCommand command;

    @Before
    public void setUp() throws Exception {
        command = new MoveCommand(player);
    }

    @Test
    public void givenCorrectCommand_whenParseAndExecute_shouldExecuteWithCorrectParameters() {
        CommandParseStatus status = command.tryParse("move to kitchen");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).moveTo("kitchen");
    }

    @Test
    public void givenCorrectCapitalizedCommand_whenParseAndExecute_shouldExecuteWithCorrectParameters() {
        CommandParseStatus status = command.tryParse("mOVE to KitChEN");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).moveTo("kitchen");
    }

    @Test
    public void givenWrongSyntaxCommand_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("move too kitchen");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenCommandWithNoLocationArgument_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("move to");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenCommandWithEmptyLocationArgument_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("move to      ");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenCorrectCommandWithSpaces_whenParseAndExecute_shouldExecuteWithCorrectlyTrimmedParameters() {
        CommandParseStatus status = command.tryParse("   move     to    kitchen");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).moveTo("kitchen");
    }

    @Test
    public void givenIncorrectCommandStartingWithPickUp_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("move toupasdasdasd");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenUnknownCommand_whenParse_shouldReturnNotRecognizedStatus() {
        CommandParseStatus status = command.tryParse("head to kitchen");

        assertEquals(CommandParseStatus.NOT_RECOGNIZED, status);
    }
}
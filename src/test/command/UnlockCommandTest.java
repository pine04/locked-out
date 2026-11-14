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
public class UnlockCommandTest {
    @Mock
    private Player player;
    private UnlockCommand command;

    @Before
    public void setUp() throws Exception {
        command = new UnlockCommand(player);
    }

    @Test
    public void givenCorrectCommand_whenParseAndExecute_shouldExecuteWithCorrectParameters() {
        CommandParseStatus status = command.tryParse("unlock laundry w/ laundry room key");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).unlockExitTo("laundry", "laundry room key");
    }

    @Test
    public void givenCorrectCapitalizedCommand_whenParseAndExecute_shouldExecuteWithCorrectParameters() {
        CommandParseStatus status = command.tryParse("UNLOCK laundry W/ LAUNDry room KEY");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).unlockExitTo("laundry", "laundry room key");
    }

    @Test
    public void givenWrongSyntaxCommand_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("unlock laundry using laundry room key");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenCommandWithNoLocationArgument_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("unlock    w/ laundry room key");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenCommandWithNoKeyArgument_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("unlock laundry w/   ");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenCorrectCommandWithSpaces_whenParseAndExecute_shouldExecuteWithCorrectlyTrimmedParameters() {
        CommandParseStatus status = command.tryParse("   unlock    laundry    w/ laundry    room   key        ");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).unlockExitTo("laundry", "laundry room key");
    }

    @Test
    public void givenIncorrectCommandStartingWithUnlock_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("unlockasdasdasd");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenUnknownCommand_whenParse_shouldReturnNotRecognizedStatus() {
        CommandParseStatus status = command.tryParse("use laundry room key on laundry");

        assertEquals(CommandParseStatus.NOT_RECOGNIZED, status);
    }
}
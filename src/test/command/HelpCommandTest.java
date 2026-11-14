package command;

import game.CommandParser;
import game.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class HelpCommandTest {
    @Mock
    private Player player;
    @Mock
    private CommandParser commandParser;
    private HelpCommand command;

    @Before
    public void setUp() throws Exception {
        command = new HelpCommand(player, commandParser);
    }

    @Test
    public void givenCorrectCommand_whenParseAndExecute_shouldExecute() {
        CommandParseStatus status = command.tryParse("help");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(commandParser).getRegisteredCommands();
    }

    @Test
    public void givenCorrectCapitalizedCommand_whenParseAndExecute_shouldExecute() {
        CommandParseStatus status = command.tryParse("HeLp");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(commandParser).getRegisteredCommands();
    }

    @Test
    public void givenWrongSyntaxCommand_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("help me plz");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenCorrectCommandWithSpaces_whenParseAndExecute_shouldExecute() {
        CommandParseStatus status = command.tryParse("   help        ");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(commandParser).getRegisteredCommands();
    }

    @Test
    public void givenIncorrectCommandStartingWithHelp_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("helpasdasdasd");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenUnknownCommand_whenParse_shouldReturnNotRecognizedStatus() {
        CommandParseStatus status = command.tryParse("information");

        assertEquals(CommandParseStatus.NOT_RECOGNIZED, status);
    }
}
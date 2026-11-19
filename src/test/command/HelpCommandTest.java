package command;

import game.CommandParser;
import game.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        Command command1 = Mockito.mock(Command.class);
        when(command1.getSyntax()).thenReturn("give <items> -> <character>");
        when(command1.getDescription()).thenReturn("Give some items in your inventory to a character.");

        Command command2 = Mockito.mock(Command.class);
        when(command2.getSyntax()).thenReturn("use <items> -> <fixture>");
        when(command2.getDescription()).thenReturn("Use some items in your inventory on a fixture.");

        when(commandParser.getRegisteredCommands()).thenReturn(List.of(command1, command2));
    }

    @Test
    public void givenCorrectCommand_whenParseAndExecute_shouldReturnCommandList() {
        CommandParseStatus status = command.tryParse("help");
        String result = command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(commandParser).getRegisteredCommands();
        assertEquals("""
                             give <items> -> <character> - Give some items in your inventory to a character.
                             use <items> -> <fixture> - Use some items in your inventory on a fixture.""", result);
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
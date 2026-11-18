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
public class LookCommandTest {
    @Mock
    private Player player;
    private LookCommand command;

    @Before
    public void setUp() throws Exception {
        command = new LookCommand(player);
    }

    @Test
    public void givenCorrectCommand_whenParseAndExecute_shouldExecute() {
        CommandParseStatus status = command.tryParse("look");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).look();
    }

    @Test
    public void givenCorrectCapitalizedCommand_whenParseAndExecute_shouldExecute() {
        CommandParseStatus status = command.tryParse("lOoK");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).look();
    }

    @Test
    public void givenWrongSyntaxCommand_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("look around");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenCorrectCommandWithSpaces_whenParseAndExecute_shouldExecute() {
        CommandParseStatus status = command.tryParse("   look        ");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).look();
    }

    @Test
    public void givenIncorrectCommandStartingWithLook_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("lookasdasdasd");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenUnknownCommand_whenParse_shouldReturnNotRecognizedStatus() {
        CommandParseStatus status = command.tryParse("see");

        assertEquals(CommandParseStatus.NOT_RECOGNIZED, status);
    }
}
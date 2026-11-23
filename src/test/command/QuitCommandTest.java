package command;

import game.Game;
import game.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class QuitCommandTest {
    @Mock
    private Player player;
    @Mock
    private Game game;
    private QuitCommand command;

    @Before
    public void setUp() throws Exception {
        command = new QuitCommand(player, game);
    }

    @Test
    public void givenCorrectCommand_whenParseAndExecute_shouldCallGameStop() {
        CommandParseStatus status = command.tryParse("quit");
        String result = command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(game).stop();
        assertEquals("Goodbye!", result);
    }

    @Test
    public void givenCorrectCapitalizedCommand_whenParseAndExecute_shouldCallGameStop() {
        CommandParseStatus status = command.tryParse("qUIt");
        String result = command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(game).stop();
        assertEquals("Goodbye!", result);
    }

    @Test
    public void givenWrongSyntaxCommand_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("quit the game");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenCorrectCommandWithSpaces_whenParseAndExecute_shouldCallGameStop() {
        CommandParseStatus status = command.tryParse("   quit        ");
        String result = command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(game).stop();
        assertEquals("Goodbye!", result);
    }

    @Test
    public void givenIncorrectCommandStartingWithQuit_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("quitasdasdasd");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenUnknownCommand_whenParse_shouldReturnNotRecognizedStatus() {
        CommandParseStatus status = command.tryParse("exit");

        assertEquals(CommandParseStatus.NOT_RECOGNIZED, status);
    }
}
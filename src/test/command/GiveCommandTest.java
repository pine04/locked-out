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
public class GiveCommandTest {
    @Mock
    private Player player;
    private GiveCommand command;

    @Before
    public void setUp() throws Exception {
        command = new GiveCommand(player);
    }

    @Test
    public void givenCorrectCommand_whenParseAndExecute_shouldExecuteWithCorrectParameters() {
        CommandParseStatus status = command.tryParse("give ring, bottle -> Alex");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).give("alex", List.of("ring", "bottle"));
    }

    @Test
    public void givenCorrectCapitalizedCommand_whenParseAndExecute_shouldExecuteWithCorrectParameters() {
        CommandParseStatus status = command.tryParse("GiVE RIng, botTLe -> AlEX");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).give("alex", List.of("ring", "bottle"));
    }

    @Test
    public void givenWrongSyntaxCommand_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("give ring, bottle to Alex");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenCommandWithNoItemArgument_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("give    -> Alex");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenCommandWithNoCharacterArgument_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("give ring ->   ");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenCommandWithEmptyItemIds_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("give   ,   ,  , -> Alex");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenCommandWithDuplicateItemIds_whenParse_shouldExecuteWithDedupedIds() {
        CommandParseStatus status = command.tryParse("give ring,ring,RiNG  ,  RIng, rING -> Alex");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).give("alex", List.of("ring"));
    }

    @Test
    public void givenCorrectCommandWithSpaces_whenParseAndExecute_shouldExecuteWithCorrectlyTrimmedParameters() {
        CommandParseStatus status = command.tryParse("   give    ring,    bottle     , phone      ->     Alex        ");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).give("alex", List.of("ring", "bottle", "phone"));
    }

    @Test
    public void givenCommandWithSomeBlankItemIds_whenParseAndExecute_shouldExecuteWithNoBlankParameters() {
        CommandParseStatus status = command.tryParse("give ring, , bottle -> Alex");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).give("alex", List.of("ring", "bottle"));
    }

    @Test
    public void givenIncorrectCommandStartingWithGive_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("giveasdasdasd");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenUnknownCommand_whenParse_shouldReturnNotRecognizedStatus() {
        CommandParseStatus status = command.tryParse("hand ring, bottle to Alex");

        assertEquals(CommandParseStatus.NOT_RECOGNIZED, status);
    }
}
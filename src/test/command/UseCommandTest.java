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
public class UseCommandTest {
    @Mock
    private Player player;
    private UseCommand command;

    @Before
    public void setUp() throws Exception {
        command = new UseCommand(player);
    }

    @Test
    public void givenCorrectCommand_whenParseAndExecute_shouldExecuteWithCorrectParameters() {
        CommandParseStatus status = command.tryParse("use pizza base, pepperoni -> oven");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).useOn("oven", List.of("pizza base", "pepperoni"));
    }

    @Test
    public void givenCorrectCapitalizedCommand_whenParseAndExecute_shouldExecuteWithCorrectParameters() {
        CommandParseStatus status = command.tryParse("USe pizZA BASe, pEPPEroni -> OVEN");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).useOn("oven", List.of("pizza base", "pepperoni"));
    }

    @Test
    public void givenWrongSyntaxCommand_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("use pizza base, pepperoni on oven");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenCommandWithNoItemArgument_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("use    -> oven");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenCommandWithNoCharacterArgument_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("use pizza base ->   ");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenCommandWithEmptyItemIds_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("use   ,   ,  , -> oven");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenCommandWithDuplicateItemIds_whenParse_shouldExecuteWithDedupedIds() {
        CommandParseStatus status = command.tryParse("use ring,ring,RiNG  ,  RIng, rING -> oven");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).useOn("oven", List.of("ring"));
    }

    @Test
    public void givenCorrectCommandWithSpaces_whenParseAndExecute_shouldExecuteWithCorrectlyTrimmedParameters() {
        CommandParseStatus status =
                command.tryParse("   use    pizza base,    tomato    sauce     , pepperoni      ->     oven        ");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).useOn("oven", List.of("pizza base", "tomato sauce", "pepperoni"));
    }

    @Test
    public void givenCommandWithSomeBlankItemIds_whenParseAndExecute_shouldExecuteWithNoBlankParameters() {
        CommandParseStatus status = command.tryParse("use pizza base, ,pepperoni  -> oven");
        command.execute();

        assertEquals(CommandParseStatus.SUCCESS, status);
        verify(player).useOn("oven", List.of("pizza base", "pepperoni"));
    }

    @Test
    public void givenIncorrectCommandStartingWithUse_whenParse_shouldReturnWrongSyntaxStatus() {
        CommandParseStatus status = command.tryParse("useasdasdasd");

        assertEquals(CommandParseStatus.WRONG_SYNTAX, status);
    }

    @Test
    public void givenUnknownCommand_whenParse_shouldReturnNotRecognizedStatus() {
        CommandParseStatus status = command.tryParse("cook pizza base, pepperoni -> oven");

        assertEquals(CommandParseStatus.NOT_RECOGNIZED, status);
    }
}
package command;

import game.Game;
import game.Player;

public class QuitCommand extends Command {
    private final Game game;

    public QuitCommand(Player player, Game game) {
        super(player);
        this.game = game;
    }

    @Override
    public String getSyntax() {
        return CommandConstants.QUIT_COMMAND_SYNTAX;
    }

    @Override
    public String getDescription() {
        return CommandConstants.QUIT_COMMAND_DESCRIPTION;
    }

    @Override
    public CommandParseStatus parse(String userInput) {
        if (userInput.equals(CommandConstants.QUIT_COMMAND_SYNTAX)) {
            return CommandParseStatus.SUCCESS;
        } else if (userInput.startsWith(CommandConstants.QUIT_COMMAND_SYNTAX)) {
            return CommandParseStatus.WRONG_SYNTAX;
        } else {
            return CommandParseStatus.NOT_RECOGNIZED;
        }
    }

    @Override
    public String execute() {
        game.stop();
        return "Goodbye!";
    }
}

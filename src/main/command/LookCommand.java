package command;

import game.Player;

public class LookCommand extends Command {
    public LookCommand(Player player) {
        super(player);
    }

    @Override
    public String getSyntax() {
        return CommandConstants.LOOK_COMMAND_SYNTAX;
    }

    @Override
    public String getDescription() {
        return CommandConstants.LOOK_COMMAND_DESCRIPTION;
    }

    @Override
    protected CommandParseStatus parse(String userInput) {
        if (userInput.equals(CommandConstants.LOOK_COMMAND_SYNTAX)) {
            return CommandParseStatus.SUCCESS;
        } else if (userInput.startsWith(CommandConstants.LOOK_COMMAND_SYNTAX)) {
            return CommandParseStatus.WRONG_SYNTAX;
        } else {
            return CommandParseStatus.NOT_RECOGNIZED;
        }
    }

    @Override
    public String execute() {
        return player.look();
    }
}

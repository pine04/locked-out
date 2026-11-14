package command;

import game.Player;

import java.util.regex.Matcher;

public class MoveCommand extends Command {
    private String locationIdentifier;

    public MoveCommand(Player player) {
        super(player);
    }

    @Override
    public String getSyntax() {
        return CommandConstants.MOVE_COMMAND_SYNTAX;
    }

    @Override
    public String getDescription() {
        return CommandConstants.MOVE_COMMAND_DESCRIPTION;
    }

    @Override
    protected CommandParseStatus parse(String userInput) {
        Matcher matcher = CommandConstants.MOVE_COMMAND_PATTERN.matcher(userInput);

        if (matcher.matches()) {
            locationIdentifier = matcher.group(1);
            return CommandParseStatus.SUCCESS;
        } else if (userInput.startsWith(CommandConstants.MOVE_COMMAND_START)) {
            return CommandParseStatus.WRONG_SYNTAX;
        } else {
            return CommandParseStatus.NOT_RECOGNIZED;
        }
    }

    @Override
    public String execute() {
        return player.moveTo(locationIdentifier);
    }
}

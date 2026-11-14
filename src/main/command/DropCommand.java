package command;

import game.Player;

import java.util.regex.Matcher;

public class DropCommand extends Command {
    private String itemIdentifier;

    public DropCommand(Player player) {
        super(player);
    }

    @Override
    public String getSyntax() {
        return CommandConstants.DROP_COMMAND_SYNTAX;
    }

    @Override
    public String getDescription() {
        return CommandConstants.DROP_COMMAND_DESCRIPTION;
    }

    @Override
    protected CommandParseStatus parse(String userInput) {
        Matcher matcher = CommandConstants.DROP_COMMAND_PATTERN.matcher(userInput);

        if (matcher.matches()) {
            itemIdentifier = matcher.group(1);
            return CommandParseStatus.SUCCESS;
        } else if (userInput.startsWith(CommandConstants.DROP_COMMAND_START)) {
            return CommandParseStatus.WRONG_SYNTAX;
        } else {
            return CommandParseStatus.NOT_RECOGNIZED;
        }
    }

    @Override
    public String execute() {
        return player.drop(itemIdentifier);
    }
}

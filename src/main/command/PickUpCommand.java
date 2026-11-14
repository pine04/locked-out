package command;

import game.Player;

import java.util.regex.Matcher;

public class PickUpCommand extends Command {
    private String itemIdentifier;

    public PickUpCommand(Player player) {
        super(player);
    }

    @Override
    public String getSyntax() {
        return CommandConstants.PICK_UP_COMMAND_SYNTAX;
    }

    @Override
    public String getDescription() {
        return CommandConstants.PICK_UP_COMMAND_DESCRIPTION;
    }

    @Override
    protected CommandParseStatus parse(String userInput) {
        Matcher matcher = CommandConstants.PICK_UP_COMMAND_PATTERN.matcher(userInput);

        if (matcher.matches()) {
            itemIdentifier = matcher.group(1).strip();
            return CommandParseStatus.SUCCESS;
        } else if (userInput.startsWith(CommandConstants.PICK_UP_COMMAND_START)) {
            return CommandParseStatus.WRONG_SYNTAX;
        } else {
            return CommandParseStatus.NOT_RECOGNIZED;
        }
    }

    @Override
    public String execute() {
        return player.pickUp(itemIdentifier);
    }
}

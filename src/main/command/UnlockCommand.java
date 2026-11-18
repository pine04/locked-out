package command;

import game.Player;

import java.util.regex.Matcher;

public class UnlockCommand extends Command {
    private String locationIdentifier;
    private String keyItemIdentifier;

    public UnlockCommand(Player player) {
        super(player);
    }

    @Override
    public String getSyntax() {
        return CommandConstants.UNLOCK_COMMAND_SYNTAX;
    }

    @Override
    public String getDescription() {
        return CommandConstants.UNLOCK_COMMAND_DESCRIPTION;
    }

    @Override
    protected CommandParseStatus parse(String userInput) {
        Matcher matcher = CommandConstants.UNLOCK_COMMAND_PATTERN.matcher(userInput);

        if (matcher.matches()) {
            locationIdentifier = matcher.group(1);
            keyItemIdentifier = matcher.group(2);
            return CommandParseStatus.SUCCESS;
        } else if (userInput.startsWith(CommandConstants.UNLOCK_COMMAND_START)) {
            return CommandParseStatus.WRONG_SYNTAX;
        } else {
            return CommandParseStatus.NOT_RECOGNIZED;
        }
    }

    @Override
    public String execute() {
        return player.unlockExitTo(locationIdentifier, keyItemIdentifier);
    }
}

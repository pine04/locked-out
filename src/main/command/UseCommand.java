package command;

import game.Player;

import java.util.List;
import java.util.regex.Matcher;

import static utils.StringUtils.splitAndDedupe;

public class UseCommand extends Command {
    private List<String> itemIdentifiers;
    private String fixtureIdentifier;

    public UseCommand(Player player) {
        super(player);
    }

    @Override
    public String getSyntax() {
        return CommandConstants.USE_COMMAND_SYNTAX;
    }

    @Override
    public String getDescription() {
        return CommandConstants.USE_COMMAND_DESCRIPTION;
    }

    @Override
    protected CommandParseStatus parse(String userInput) {
        Matcher matcher = CommandConstants.USE_COMMAND_PATTERN.matcher(userInput);

        if (matcher.matches()) {
            itemIdentifiers = splitAndDedupe(matcher.group(1), ",");
            fixtureIdentifier = matcher.group(2).strip();

            if (itemIdentifiers.isEmpty() || fixtureIdentifier.isBlank()) {
                return CommandParseStatus.WRONG_SYNTAX;
            }

            return CommandParseStatus.SUCCESS;
        } else if (userInput.startsWith(CommandConstants.USE_COMMAND_START)) {
            return CommandParseStatus.WRONG_SYNTAX;
        } else {
            return CommandParseStatus.NOT_RECOGNIZED;
        }
    }

    @Override
    public String execute() {
        return player.useOn(fixtureIdentifier, itemIdentifiers);
    }
}

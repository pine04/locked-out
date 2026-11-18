package command;

import game.Player;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

public class GiveCommand extends Command {
    private List<String> itemIdentifiers;
    private String characterIdentifier;

    public GiveCommand(Player player) {
        super(player);
    }

    @Override
    public String getSyntax() {
        return CommandConstants.GIVE_COMMAND_SYNTAX;
    }

    @Override
    public String getDescription() {
        return CommandConstants.GIVE_COMMAND_DESCRIPTION;
    }

    @Override
    protected CommandParseStatus parse(String userInput) {
        Matcher matcher = CommandConstants.GIVE_COMMAND_PATTERN.matcher(userInput);

        if (matcher.matches()) {
            itemIdentifiers = Arrays.stream(matcher.group(1).split(",")).map(String::strip).filter(s -> !s.isBlank()).toList();
            characterIdentifier = matcher.group(2).strip();

            if (itemIdentifiers.isEmpty() || characterIdentifier.isBlank()) {
                return CommandParseStatus.WRONG_SYNTAX;
            }

            return CommandParseStatus.SUCCESS;
        } else if (userInput.startsWith(CommandConstants.GIVE_COMMAND_START)) {
            return CommandParseStatus.WRONG_SYNTAX;
        } else {
            return CommandParseStatus.NOT_RECOGNIZED;
        }
    }

    @Override
    public String execute() {
        return player.give(characterIdentifier, itemIdentifiers);
    }
}

package command;

import game.Player;

import java.util.regex.Matcher;

public class TalkCommand extends Command {
    private String characterIdentifier;

    public TalkCommand(Player player) {
        super(player);
    }

    @Override
    public String getSyntax() {
        return CommandConstants.TALK_TO_COMMAND_SYNTAX;
    }

    @Override
    public String getDescription() {
        return CommandConstants.TALK_TO_COMMAND_DESCRIPTION;
    }

    @Override
    protected CommandParseStatus parse(String userInput) {
        Matcher matcher = CommandConstants.TALK_TO_COMMAND_PATTERN.matcher(userInput);

        if (matcher.matches()) {
            characterIdentifier = matcher.group(1);
            return CommandParseStatus.SUCCESS;
        } else if (userInput.startsWith(CommandConstants.TALK_TO_COMMAND_START)) {
            return CommandParseStatus.WRONG_SYNTAX;
        } else {
            return CommandParseStatus.NOT_RECOGNIZED;
        }
    }

    @Override
    public String execute() {
        return player.talkTo(characterIdentifier);
    }
}

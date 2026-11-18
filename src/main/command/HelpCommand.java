package command;

import game.CommandParser;
import game.Player;

import java.util.stream.Collectors;

public class HelpCommand extends Command {
    private final CommandParser commandParser;

    public HelpCommand(Player player, CommandParser commandParser) {
        super(player);
        this.commandParser = commandParser;
    }

    @Override
    public String getSyntax() {
        return CommandConstants.HELP_COMMAND_SYNTAX;
    }

    @Override
    public String getDescription() {
        return CommandConstants.HELP_COMMAND_DESCRIPTION;
    }

    @Override
    public CommandParseStatus parse(String userInput) {
        if (userInput.equals(CommandConstants.HELP_COMMAND_SYNTAX)) {
            return CommandParseStatus.SUCCESS;
        } else if (userInput.startsWith(CommandConstants.HELP_COMMAND_SYNTAX)) {
            return CommandParseStatus.WRONG_SYNTAX;
        } else {
            return CommandParseStatus.NOT_RECOGNIZED;
        }
    }

    @Override
    public String execute() {
        return commandParser.getRegisteredCommands().stream().map(command -> String.format("%s - %s", command.getSyntax(), command.getDescription())).collect(Collectors.joining("\n"));
    }
}

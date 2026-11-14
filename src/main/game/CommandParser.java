package game;

import command.Command;
import command.CommandParseResult;
import command.CommandParseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandParser {
    private final List<Command> commandRegistry;

    public CommandParser() {
        commandRegistry = new ArrayList<>();
    }

    public List<Command> getRegisteredCommands() {
        return List.copyOf(commandRegistry);
    }

    public void registerCommand(Command command) {
        commandRegistry.add(command);
    }

    public CommandParseResult parse(String userInput) {
        for (Command command : commandRegistry) {
            CommandParseStatus status = command.tryParse(userInput);
            if (status == CommandParseStatus.SUCCESS) {
                return new CommandParseResult(CommandParseStatus.SUCCESS, Optional.of(command));
            } else if (status == CommandParseStatus.WRONG_SYNTAX) {
                return new CommandParseResult(CommandParseStatus.WRONG_SYNTAX, Optional.empty());
            }
        }

        return new CommandParseResult(CommandParseStatus.NOT_RECOGNIZED, Optional.empty());
    }
}

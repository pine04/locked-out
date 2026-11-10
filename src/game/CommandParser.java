package game;

import command.Command;

import java.util.List;
import java.util.Optional;

public class CommandParser {
    private List<Command> commandRegistry;

    public CommandParser() {

    }

    public void registerCommand(Command command) {
        commandRegistry.add(command);
    }

    public Optional<Command> parse(String userInput) {
        return Optional.empty();
    }
}

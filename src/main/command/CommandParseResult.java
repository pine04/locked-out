package command;

import java.util.Optional;

public record CommandParseResult(CommandParseStatus status, Optional<Command> command) {
}

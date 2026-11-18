package command;

import game.Player;

public abstract class Command {
    protected final Player player;

    public Command(Player player) {
        this.player = player;
    }

    public abstract String getSyntax();

    public abstract String getDescription();

    public final CommandParseStatus tryParse(String userInput) {
        String cleanInput = preProcess(userInput);
        return parse(cleanInput);
    }

    public abstract String execute();

    private String preProcess(String userInput) {
        return userInput.trim().toLowerCase().replaceAll("\\s+", " ");
    }

    protected abstract CommandParseStatus parse(String userInput);
}

package command;

import game.Player;

public abstract class Command {
    private final Player player;

    public Command(Player player) {
        this.player = player;
    }

    public abstract boolean tryParse(String userInput);

    public abstract void execute();
}

package command;

import game.Player;

public class DropCommand extends Command {
    private String itemIdentifier;

    public DropCommand(Player player) {
        super(player);
    }

    @Override
    public boolean tryParse(String userInput) {
        return false;
    }

    @Override
    public void execute() {

    }
}

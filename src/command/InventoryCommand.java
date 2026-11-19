package command;

import game.Player;

public class InventoryCommand extends Command {
    public InventoryCommand(Player player) {
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

package command;

import game.Player;

public class PickUpCommand extends Command {
    private String itemIdentifier;

    public PickUpCommand(Player player) {
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

package command;

import game.Player;

public class MoveCommand extends Command {
    private String locationIdentifier;

    public MoveCommand(Player player) {
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

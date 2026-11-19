package command;

import game.Player;

public class UnlockCommand extends Command {
    private String keyItemIdentifier;
    private String locationIdentifier;

    public UnlockCommand(Player player) {
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

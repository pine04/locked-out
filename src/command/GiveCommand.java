package command;

import game.Player;

import java.util.List;

public class GiveCommand extends Command {
    private String characterIdentifier;
    private List<String> itemsIdentifier;

    public GiveCommand(Player player) {
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

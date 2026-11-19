package command;

import game.Player;

import java.util.List;

public class UseCommand extends Command {
    private List<String> itemIdentifiers;
    private String fixtureIdentifier;

    public UseCommand(Player player) {
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

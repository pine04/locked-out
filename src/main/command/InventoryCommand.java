package command;

import game.Player;

public class InventoryCommand extends Command {
    public InventoryCommand(Player player) {
        super(player);
    }

    @Override
    public String getSyntax() {
        return CommandConstants.INVENTORY_COMMAND_SYNTAX;
    }

    @Override
    public String getDescription() {
        return CommandConstants.INVENTORY_COMMAND_DESCRIPTION;
    }

    @Override
    protected CommandParseStatus parse(String userInput) {
        if (userInput.equals(CommandConstants.INVENTORY_COMMAND_SYNTAX)) {
            return CommandParseStatus.SUCCESS;
        } else if (userInput.startsWith(CommandConstants.INVENTORY_COMMAND_SYNTAX)) {
            return CommandParseStatus.WRONG_SYNTAX;
        } else {
            return CommandParseStatus.NOT_RECOGNIZED;
        }
    }

    @Override
    public String execute() {
        return player.showInventory();
    }
}

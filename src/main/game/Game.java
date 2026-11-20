package game;

import command.CommandParseResult;

import java.util.Scanner;
//abdullah added these too add there part in it :
import command.HelpCommand;
import command.LookCommand;
import command.PickUpCommand;
import command.InventoryCommand;


public class Game {
    private Player player;
    private CommandParser commandParser;
    private boolean hasWon;

    public void setUp() {
        //Initialize the parser
        commandParser = new CommandParser();

        // 2. Setup the world
        Location startRoom = new Location(
                "hallway",
                "You are standing in a dimly lit hallway.");
        startRoom.addItem(new Item(
                "ring",
                "A shiny gold ring.")
        );
        startRoom.addItem(new Item(
                "torch",
                "A burning torch.")
        );

        //Setup the player (with inventory)inventory size is 15
        Inventory playerInventory = new Inventory(15);
        player = new Player(startRoom, playerInventory);

        // Register all commands
        commandParser.registerCommand(new HelpCommand(player, commandParser));
        //becasue of line 10 i need to change this as
        commandParser.registerCommand(new LookCommand(player));
        commandParser.registerCommand(new PickUpCommand(player));
        commandParser.registerCommand(new InventoryCommand(player));

        System.out.println("Welcome to Locked Out! Type 'help' to start.");
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        String input;

        while (!hasWon) {
            System.out.print("> ");
            input = scanner.nextLine();
            CommandParseResult parseResult = commandParser.parse(input);
            switch (parseResult.status()) {
                case SUCCESS -> parseResult.command().ifPresent(command -> System.out.println(command.execute()));
                case WRONG_SYNTAX -> System.out.println("Wrong syntax. Type 'help' for more information.");
                case NOT_RECOGNIZED -> System.out.println("Command not recognized. Type 'help' for more information.");
            }
        }
    }

    public void win() {
        hasWon = true;
        System.out.println("--CONGRATULATIONS YOU WIN--");
    }
}

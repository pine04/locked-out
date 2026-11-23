package game;

import command.*;

import java.util.Scanner;

public class Game {
    private Player player;
    private CommandParser commandParser;
    private boolean hasWon;
    private boolean isRunning;

    public Game() {
        hasWon = false;
        isRunning = true;
    }

    public void setUp() {
        commandParser = new CommandParser();

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

        Inventory playerInventory = new Inventory(15);
        player = new Player(startRoom, playerInventory);

        commandParser.registerCommand(new HelpCommand(player, commandParser));
        commandParser.registerCommand(new LookCommand(player));
        commandParser.registerCommand(new PickUpCommand(player));
        commandParser.registerCommand(new InventoryCommand(player));
        commandParser.registerCommand(new DropCommand(player));
        commandParser.registerCommand(new TalkCommand(player));
        commandParser.registerCommand(new QuitCommand(player, this));

        System.out.println("Welcome to Locked Out! Type 'help' to start.");
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        String input;

        while (!hasWon && isRunning) {
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

    public void stop() {
        isRunning = false;
    }
}

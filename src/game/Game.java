package game;

import command.Command;
import command.HelpCommand;
import command.LookCommand;
import java.util.Optional;
import java.util.Scanner;

public class Game {
    private Player player;
    private CommandParser commandParser;
    private boolean hasWon;
    private boolean isRunning = true;

    public void setUp() {
        commandParser = new CommandParser();

        Location startRoom = new Location("hallway", "You are standing in a dimly lit hallway.");
        startRoom.addItem(new Item("ring", "A shiny gold ring.")); // this i making issue pelase check it on your side in file as i added the comment in tHE LOCATION FILE TOO
        startRoom.addItem(new Item("torch", "A burning torch."));// THESE TWO --
        player = new Player(startRoom);

        // --- REGISTER COMMANDS ---
        commandParser.registerCommand(new HelpCommand(player));
        commandParser.registerCommand(new LookCommand(player)); // Look command added!
        System.out.println("Welcome to Locked Out! Type 'help' to start.");
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        while (isRunning && !hasWon) {
            System.out.print("> ");
            String input = scanner.nextLine();

            if (input.trim().equalsIgnoreCase("quit")) {
                System.out.println("Goodbye!");
                break;
            }

            Optional<Command> command = commandParser.parse(input);
            if (command.isPresent()) {
                command.get().execute();
            } else {
                System.out.println("I don't understand I THINK I NEED HELP'" + input + "'. Type 'help'.");
            }
        }
    }

    public void win() {
        this.hasWon = true;
        System.out.println("----CONGRATULATION YOU WIN ---");
    }
}

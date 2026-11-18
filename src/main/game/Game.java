package game;

import command.CommandParseResult;

import java.util.Scanner;

public class Game {
    private Player player;
    private CommandParser commandParser;
    private boolean hasWon;

    public void setUp() {

    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        String input;

        while (!hasWon) {
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
    }
}

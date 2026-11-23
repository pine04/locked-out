package command;

import java.util.regex.Pattern;

public class CommandConstants {
    public static final String HELP_COMMAND_SYNTAX = "help";
    public static final String HELP_COMMAND_DESCRIPTION = "Show a list of actions that the player can perform.";

    public static final String QUIT_COMMAND_SYNTAX = "quit";
    public static final String QUIT_COMMAND_DESCRIPTION = "Quit the game. Your progress is not saved.";

    public static final String LOOK_COMMAND_SYNTAX = "look";
    public static final String LOOK_COMMAND_DESCRIPTION = "Show information about the player's current location.";

    public static final String PICK_UP_COMMAND_SYNTAX = "pick up <item>";
    public static final String PICK_UP_COMMAND_DESCRIPTION = "Pick up an item at the player's location. Example: pick up light";
    public static final Pattern PICK_UP_COMMAND_PATTERN = Pattern.compile("^pick up\\s+(.+)$", Pattern.CASE_INSENSITIVE);
    public static final String PICK_UP_COMMAND_START = "pick up";

    public static final String INVENTORY_COMMAND_SYNTAX = "inventory";
    public static final String INVENTORY_COMMAND_DESCRIPTION = "List the items in the player's inventory.";

    public static final String DROP_COMMAND_SYNTAX = "drop <item>";
    public static final String DROP_COMMAND_DESCRIPTION = "Drop an item from the player's inventory. Example: drop light";
    public static final Pattern DROP_COMMAND_PATTERN = Pattern.compile("^drop\\s+(.+)$", Pattern.CASE_INSENSITIVE);
    public static final String DROP_COMMAND_START = "drop";

    public static final String TALK_TO_COMMAND_SYNTAX = "talk to <character>";
    public static final String TALK_TO_COMMAND_DESCRIPTION = "Talk to a character. Example: talk to Alex";
    public static final Pattern TALK_TO_COMMAND_PATTERN = Pattern.compile("^talk to\\s+(.+)$", Pattern.CASE_INSENSITIVE);
    public static final String TALK_TO_COMMAND_START = "talk to";

    public static final String GIVE_COMMAND_SYNTAX = "give <comma-separated list of items> -> <character>";
    public static final String GIVE_COMMAND_DESCRIPTION = "Give one ore more items to a character. Example: give light, phone -> Alex";
    public static final Pattern GIVE_COMMAND_PATTERN = Pattern.compile("^give\\s+(.+?)\\s+->\\s+(.+)$", Pattern.CASE_INSENSITIVE);
    public static final String GIVE_COMMAND_START = "give";

    public static final String USE_COMMAND_SYNTAX = "use <comma-separated list of items> -> <fixture>";
    public static final String USE_COMMAND_DESCRIPTION = "Use one or more items on a fixture. Example: use battery -> TV";
    public static final Pattern USE_COMMAND_PATTERN = Pattern.compile("^use\\s+(.+?)\\s+->\\s+(.+)$", Pattern.CASE_INSENSITIVE);
    public static final String USE_COMMAND_START = "use";

    public static final String MOVE_COMMAND_SYNTAX = "move to <location>";
    public static final String MOVE_COMMAND_DESCRIPTION = "Move to a new location. Example: move to bedroom";
    public static final Pattern MOVE_COMMAND_PATTERN = Pattern.compile("^move to\\s+(.+)$", Pattern.CASE_INSENSITIVE);
    public static final String MOVE_COMMAND_START = "move to";

    public static final String UNLOCK_COMMAND_SYNTAX = "unlock <location> w/ <key>";
    public static final String UNLOCK_COMMAND_DESCRIPTION = "Unlocks the location with the given key. Example: unlock bedroom w/ master key";
    public static final Pattern UNLOCK_COMMAND_PATTERN = Pattern.compile("^unlock\\s+(.+?)\\s+w/\\s+(.+)$", Pattern.CASE_INSENSITIVE);
    public static final String UNLOCK_COMMAND_START = "unlock";
}

package util;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class InputPrompter {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static String promptString(String m) throws NoSuchElementException {
        ConsoleLog.log(m, true);
        return InputPrompter.SCANNER.nextLine().trim();
    }

    public static String promptValidatedString(String promptMessage, Validator<String> v) {
        String input;
        while(true) {
            input =  InputPrompter.promptString(promptMessage);
            if(v.validate(input)) break;
            ConsoleLog.error("Invalid input. Please enter a valid input to continue.");
        }

        return input;
    }

    public static int promptInt(String m) {
        try {
            String input = InputPrompter.promptString(m);
            return Integer.parseInt(input);
        }catch (NumberFormatException e) {
            ConsoleLog.error("Invalid input type. Integer required");
        }
        return 0;
    }

    public static int promptInt(String m, int rangeStart, int rangeEnd) {
        while(true) {
            int input = InputPrompter.promptInt(m);
            if(input > rangeStart && input < rangeEnd) return input;

            ConsoleLog.error(String.format("Provided integer value is out of range. Please provide an integer between %d and %d", rangeStart, rangeEnd), true);
        }
    }

    public static float promptFloat(String m) {
        try {
            String input = InputPrompter.promptString(m);
            return Float.parseFloat(input);
        }catch (NumberFormatException e) {
            ConsoleLog.error("Invalid input type. Float required", true);
            throw new Error();
        }
    }

    public static float promptFloat(String m, float rangeStart, float rangeEnd) {
        while(true) {
            float input = InputPrompter.promptFloat(m);
            if(input > rangeStart && input < rangeEnd) return input;

            ConsoleLog.error(String.format("Provided float value is out of range. Please provide a float between %f and %f", rangeStart, rangeEnd), true);
        }
    }
}

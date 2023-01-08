package util;

public class ConsoleLog {
    public static void log(String m) {
        System.out.println(m);
    }

    public static void log(String m, boolean isSameLine) {
        if(isSameLine) System.out.print(m);
        else log(m);
    }

    public static void logWithColors(String color, String m) {
        System.out.print(color);
        ConsoleLog.log(m,true);
        System.out.println(ConsoleColors.RESET);
    }
    public static void logWithColors(String color, String m, boolean isSameLine) {
        System.out.print(color);
        ConsoleLog.log(m, isSameLine);
        System.out.print(ConsoleColors.RESET);
    }

    public static void error(String m) {
        ConsoleLog.logWithColors(ConsoleColors.RED, m);
    }
    public static void error(String m, boolean isSameLine) {
        ConsoleLog.logWithColors(ConsoleColors.RED, m, isSameLine);
    }

    public static void success(String m) {
        ConsoleLog.logWithColors(ConsoleColors.GREEN, m);
    }
    public static void success(String m, boolean isSameLine) {
        ConsoleLog.logWithColors(ConsoleColors.GREEN_BRIGHT, m, isSameLine);
    }

    public static void warning(String m) {
        ConsoleLog.logWithColors(ConsoleColors.YELLOW, m);
    }
    public static void warning(String m, boolean isSameLine) {
        ConsoleLog.logWithColors(ConsoleColors.YELLOW, m, isSameLine);
    }

    public static void info(String m) {
        ConsoleLog.logWithColors(ConsoleColors.BLUE, m);
    }
    public static void info(String m,boolean isSameLine) {
        ConsoleLog.logWithColors(ConsoleColors.BLUE, m, isSameLine);
    }
}

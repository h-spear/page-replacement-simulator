package etc;

import settings.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static etc.IOFont.*;

public class IO {
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void println(String color, String message) {
        if (Settings.COLOR_MODE) {
            System.out.println(color + message + RESET);
        } else {
            System.out.println(message);
        }
    }

    public static void printf(String color, String message) {
        if (Settings.COLOR_MODE) {
            System.out.printf(color + message + RESET);
        } else {
            System.out.printf(message);
        }
    }

    public static String input() throws IOException {
        System.out.printf("~ ");
        return br.readLine().trim();
    }

    public static int inputNumber() throws IOException {
        System.out.printf("~ ");
        String input = br.readLine().trim();
        if (!IO.isNumeric(input)) {
            IO.println(FONT_RED, "please enter numbers only.");
            return inputNumber();
        }
        return Integer.parseInt(input);
    }

    public static String centerAlign(String msg, int length) {
        int trimCount = length - msg.length();
        int rightTrimCount = trimCount / 2;
        int leftTrimCount = trimCount - rightTrimCount;

        String result = "";
        result += " ".repeat(leftTrimCount);
        result += msg;
        result += " ".repeat(rightTrimCount);
        return result;
    }

    public static String centerAlignAndEffect(String msg, int length, String effect) {
        int trimCount = length - msg.length();
        int rightTrimCount = trimCount / 2;
        int leftTrimCount = trimCount - rightTrimCount;

        String result = "";
        result += " ".repeat(leftTrimCount);
        result += effect;
        result += msg;
        result += RESET;
        result += " ".repeat(rightTrimCount);
        return result;
    }

    public static boolean isNumeric(String string) {
        return string.chars().allMatch(Character :: isDigit);
    }
}

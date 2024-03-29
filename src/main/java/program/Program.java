package program;

import enums.Mode;
import file.FileChooser;
import static etc.IO.*;
import static etc.IOFont.*;

import file.FileManager;
import settings.Settings;
import simulator.*;
import enums.Policy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Program {
    private int bufferSize;
    private List<Simulator> simulators;
    private File file;
    private long[] stream;
    private String input;
    private Mode mode;
    private boolean animation;
    private boolean output;
    private String directory;

    public void execute() throws IOException {
        while (true) {
            if (!selectMode())
                break;
            selectBufferSize();
            selectPolicy();
            selectAnimation();
            selectDirectory();
            simulation();

            // replay
            System.out.println();
            println(FONT_GREEN, "replay? (1: yes, other: no)");
            if (inputNumber() != 1) {
                break;
            }
        }
    }

    private void simulation() throws IOException {
        for (Simulator simulator: simulators) {
            // 모드에 맞게 실행.
            if (mode == Mode.DIRECT) {
                if (output)
                    simulator.simulate(stream, animation, new File(directory + FileManager.getFileName(simulator.getName())));
                else
                    simulator.simulate(stream, animation);
            } else if (mode == Mode.FILE) {
                try {
                    if (output)
                        simulator.simulate(file, new File(directory + FileManager.getFileName(simulator.getName())));
                    else
                        simulator.simulate(file);
                } catch (NumberFormatException e) {
                    println(FONT_RED, "the data format is invalid.\n");
                }
            }
        }
    }

    private boolean selectMode() throws IOException {
        while (true) {
            System.out.println();
            printf(FONT_GREEN, "select data input mode. (");
            for (Mode mode: Mode.values()) {
                printf(FONT_GREEN, mode.ordinal() + ": " + Mode.getTitle(mode) + ", ");
            }
            println(FONT_GREEN, "99: terminate)");
            int i = inputNumber();
            Mode iMode = Mode.getMode(i);
            if (iMode == Mode.DIRECT) {
                directInput();
                break;
            } else if (iMode == Mode.FILE) {
                if (fileUpload())
                    break;
                continue;
            } else if (iMode == Mode.PREV) {
                if (mode == null) {
                    println(FONT_RED, "can not use prev data.");
                    continue;
                }
                break;
            } else if (i == 99) {
                return false;
            } else {
                println(FONT_RED, "unknown command.");
                continue;
            }
        }
        return true;
    }

    private boolean selectBufferSize() throws IOException {
        while (true) {
            System.out.println();
            println(FONT_GREEN, "enter buffer size.");
            input = input();
            if (!isNumeric(input)) {
                println(FONT_RED, "please enter numbers only.");
                continue;
            }

            bufferSize = Integer.parseInt(input);
            if (bufferSize < 0) {
                println(FONT_RED, "please enter positive number.");
                continue;
            }
            break;
        }
        return true;
    }

    private boolean selectPolicy() throws IOException {
        simulators = new ArrayList<>();
        while (true) {
            System.out.println();
            println(FONT_GREEN, "select page replacement policy.(multiple select possible)");
            for (Policy policy: Policy.values()) {
                if (policy == Policy.Optimal) {
                    printf(RESET, " " + policy.ordinal() + ": " + policy);
                    println(FONT_RED, " [O(n^2) : cannot operate if stream size > " + Settings.MAX_OPTIMAL_STREAM_SIZE + "]");
                    continue;
                }
                println(RESET, " " + policy.ordinal() + ": " + policy);
            }

            String[] strings = input().split("[ ]+");
            for (String string: strings) {
                if (!isNumeric(string)) {
                    continue;
                }
                int i = Integer.parseInt(string);
                Simulator simulator = Policy.getSimulator(i, bufferSize);
                if (simulator != null)
                    simulators.add(simulator);
            }

            if (simulators.isEmpty()) {
                println(FONT_RED, "retry.");
                continue;
            }
            break;
        }
        return true;
    }

    private boolean selectAnimation() throws IOException {
        if (mode == Mode.FILE)
            return false;

        System.out.println();
        println(FONT_GREEN, "do you want animation? (1: yes, other: no)");
        if (inputNumber() != 1) {
            animation = false;
        } else {
            animation = true;
        }
        return true;
    }

    private boolean selectDirectory() throws IOException {
        System.out.println();
        println(FONT_GREEN, "do you want output excel file? (1: yes, other: no)");
        if (inputNumber() != 1) {
            output = false;
            return false;
        }

        directory = FileChooser.chooseDirectory();
        if (directory == null) {
            System.out.println();
            println(FONT_RED, "canceled. do not generate output.");
            output = false;
            return false;
        }
        output = true;
        return true;
    }

    private boolean directInput() throws IOException {
        while (true) {
            System.out.println();
            println(FONT_GREEN, "enter positive number stream.");
            println(FONT_RED, " * the number can not exceed 99999999");
            println(FONT_RED, " * separated by blank");
            println(FONT_RED, " * show animation");
            String[] arr = input().split("[ ,]+");
            try {
                stream = Arrays.stream(arr)
                        .map(data -> Long.parseLong(data))
                        .filter(data -> (data <= 99999999 && data >= 0))
                        .mapToLong(Number::longValue).toArray();

                mode = Mode.DIRECT;
                return true;
            } catch (NumberFormatException e) {
                println(FONT_RED, "number error. retry.");
                continue;
            }
        }
    }

    private boolean fileUpload() {
        System.out.println();
        println(FONT_GREEN, "choose data stream txt file.");
        println(FONT_RED, " * data type is Long");
        println(FONT_RED, " * separated by lines");
        println(FONT_RED, " * do not show animation");
        file = FileChooser.choose();
        if (file == null) {
            System.out.println();
            println(FONT_RED, "canceled.");
            return false;
        }
        mode = Mode.FILE;
        return true;
    }
}

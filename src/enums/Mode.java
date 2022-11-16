package enums;

public enum Mode {
    DIRECT, FILE, PREV;

    private static Mode[] modes = Mode.values();

    public static Mode getMode(int i) {
        if (i >= modes.length) {
            return null;
        }
        return modes[i];
    }

    public static String getTitle(Mode mode) {
        if (mode == DIRECT) {
            return "direct input";
        } else if (mode == FILE) {
            return "file upload";
        } else if (mode == PREV) {
            return "use prev data";
        }
        return "";
    }
}

package enums;

public enum Mode {
    DIRECT, FILE, PREV;

    private static Mode[] list = Mode.values();

    public static Mode getMode(int i) {
        if (i >= getLength()) {
            return null;
        }
        return list[i];
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

    public static int getLength() {
        return list.length;
    }
}

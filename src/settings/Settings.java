package settings;

public class Settings {
    // 로그 색상 표시
    public static final boolean COLOR_MODE = true;

    // Optimal Simulator를 동작시킬 수 있는 최대 스트림 길이 설정
    public static final int MAX_OPTIMAL_STREAM_SIZE = 10000;

    // 시뮬레이터 이름
    public static final String FIFO_SIMULATOR_NAME = "FIFO";
    public static final String LRU_SIMULATOR_NAME = "LRU";
    public static final String LFU_SIMULATOR_NAME = "LFU";
    public static final String MFU_SIMULATOR_NAME = "MFU";
    public static final String OPT_SIMULATOR_NAME = "optimal";
    public static final String CLOCK_SIMULATOR_NAME = "clock";
    public static final String RANDOM_SIMULATOR_NAME = "random";
}

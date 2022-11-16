package enums;

import simulator.*;

public enum Policy {
    FIFO, LRU, LFU, MFU, Random, Clock, Optimal;

    private static Policy[] policies = Policy.values();

    public static Simulator getSimulator(int i, int bufferSize) {
        if (i >= getLength()){
            return null;
        }

        if (i == FIFO.ordinal())
            return new FIFOSimulator(bufferSize);
        else if(i == LRU.ordinal())
            return new LRUSimulator(bufferSize);
        else if (i == LFU.ordinal())
            return new LFUSimulator(bufferSize);
        else if (i == MFU.ordinal())
            return new MFUSimulator(bufferSize);
        else if (i == Random.ordinal())
            return new RandomSimulator(bufferSize);
        else if (i == Clock.ordinal())
            return new ClockSimulator(bufferSize);
        else if (i == Optimal.ordinal())
            return new OptimalSimulator(bufferSize);
        return null;
    }

    public static int getLength() {
        return policies.length;
    }
}

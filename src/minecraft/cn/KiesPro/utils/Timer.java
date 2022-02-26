package cn.KiesPro.utils;

public class Timer {
    static long prevMS;
    private long time;
    protected static long lastMS;
    private long lastMS1;

    public Timer() {
        prevMS = 0L;
        lastMS = -1L;
        this.time = System.nanoTime() / 1000000L;
    }

    public static boolean delay(float milliSec) {
        if ((float)(Timer.getTime() - prevMS) >= milliSec) {
            return true;
        }
        return false;
    }

    public static boolean delay(double milliSec) {
        if ((double)(Timer.getTime() - prevMS) >= milliSec) {
            return true;
        }
        return false;
    }

    public boolean delay1(float milliSec) {
        if ((float)(Timer.getTime() - prevMS) >= milliSec) {
            return true;
        }
        return false;
    }

    public void reset1() {
        this.lastMS1 = Timer.getCurrentMS();
    }

    public static void reset() {
        prevMS = Timer.getTime();
    }

    public static long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public long time() {
        return System.nanoTime() / 1000000L - this.time;
    }

    public long getDifference() {
        return Timer.getTime() - prevMS;
    }

    public void setDifference(long difference) {
        prevMS = Timer.getTime() - difference;
    }

    public boolean hasTimeElapsed(long time, boolean reset) {
        if (this.time() >= time) {
            if (reset) {
                Timer.reset();
            }
            return true;
        }
        return false;
    }

    public void setLastMS() {
        lastMS = System.currentTimeMillis();
    }

    public static boolean isDelayComplete(long delay) {
        if (System.currentTimeMillis() - lastMS >= delay) {
            return true;
        }
        return false;
    }

    public long getLastMs() {
        return System.currentTimeMillis() - lastMS;
    }

    public static long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public long getLastMS() {
        return lastMS;
    }

    public static boolean hasReached(long milliseconds) {
        if (Timer.getCurrentMS() - lastMS >= milliseconds) {
            return true;
        }
        return false;
    }

    public void setLastMS(long currentMS) {
        lastMS = currentMS;
    }

    public boolean hasReached(double milliseconds) {
        if ((double)(Timer.getCurrentMS() - lastMS) >= milliseconds) {
            return true;
        }
        return false;
    }

    public boolean hasTicksElapsed(int ticks) {
        if (this.time() >= (1000 / ticks) - 50)
            return true;
        return false;
    }
    
    public boolean hasTicksElapsed(int time, int ticks) {
        if (this.time() >= (time / ticks) - 50)
            return true;
        return false;
    }
}



package de.tum.in.ase.eist.gameview;

public class GameTimer {

    private static final int SEC = 1000;
    private static final int MIN = 60;

    private long startTime = System.currentTimeMillis();

    public void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public String toReadable() {
        long deltaTime = System.currentTimeMillis() - startTime;
        long minutes = (deltaTime / SEC) / MIN;
        long seconds = (deltaTime / SEC) % MIN;
        return minutes + " : " + seconds;
    }
}

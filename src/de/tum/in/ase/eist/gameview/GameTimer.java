package de.tum.in.ase.eist.gameview;

public class GameTimer {

    private long startTime = System.currentTimeMillis();

    public void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public String toReadable() {
        long deltaTime = System.currentTimeMillis() - startTime;
        long minutes = (deltaTime / 1000) / 60;
        long seconds = (deltaTime / 1000) % 60;
        return minutes + " : " + seconds;
    }
}

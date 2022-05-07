package de.tum.in.ase.eist.car;

import de.tum.in.ase.eist.Dimension2D;

public class PlayerCar extends Car {

    private static final String PLAYER_CAR_IMAGE_FILE = "PlayerCarSteve.jpg";

    private static final int MIN_SPEED_PLAYER_CAR = 200;
    private static final int MAX_SPEED_PLAYER_CAR = 1000;

    public PlayerCar(Dimension2D gameBoardSize) {
        super(gameBoardSize);
        setMinSpeed(MIN_SPEED_PLAYER_CAR);
        setMaxSpeed(MAX_SPEED_PLAYER_CAR);
        setRandomSpeed();
        setIconLocation(PLAYER_CAR_IMAGE_FILE);
    }
}

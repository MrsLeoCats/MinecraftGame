package de.tum.in.ase.eist.car;

import de.tum.in.ase.eist.Dimension2D;

public class EndermanCar extends Car {

    private static final String ENDERMAN_CAR_IMAGE_FILE = "EndermanCar.jpg";

    private static final int MIN_SPEED_ENDERMAN_CAR = 100;
    private static final int MAX_SPEED_ENDERMAN_CAR = 400;

    public EndermanCar(Dimension2D gameBoardSize) {
        super(gameBoardSize);
        setMinSpeed(MIN_SPEED_ENDERMAN_CAR);
        setMaxSpeed(MAX_SPEED_ENDERMAN_CAR);
        setRandomSpeed();
        setIconLocation(ENDERMAN_CAR_IMAGE_FILE);
    }
}

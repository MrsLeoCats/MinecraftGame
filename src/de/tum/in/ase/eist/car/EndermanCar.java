package de.tum.in.ase.eist.car;

import de.tum.in.ase.eist.Dimension2D;

public class EndermanCar extends Car {

    private static final String ENDERMAN_CAR_IMAGE_FILE = "EndermanCar.jpg";

    private static final double MIN_SPEED_ENDERMAN_CAR = 1.0;
    private static final double MAX_SPEED_ENDERMAN_CAR = 4.0;

    public EndermanCar(Dimension2D gameBoardSize) {
        super(gameBoardSize);
        setMinSpeed(MIN_SPEED_ENDERMAN_CAR);
        setMaxSpeed(MAX_SPEED_ENDERMAN_CAR);
        setRandomSpeed();
        setIconLocation(ENDERMAN_CAR_IMAGE_FILE);
    }
}

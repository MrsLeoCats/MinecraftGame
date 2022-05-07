package de.tum.in.ase.eist.car;

import de.tum.in.ase.eist.Dimension2D;

public class ZombieCar extends Car {

	private static final String ZOMBIE_CAR_IMAGE_FILE = "ZombieCar.jpg";

	private static final int MIN_SPEED_ZOMBIE_CAR = 200;
	private static final int MAX_SPEED_ZOMBIE_CAR = 1000;

	public ZombieCar(Dimension2D gameBoardSize) {
		super(gameBoardSize);
		setMinSpeed(MIN_SPEED_ZOMBIE_CAR);
		setMaxSpeed(MAX_SPEED_ZOMBIE_CAR);
		setRandomSpeed();
		setIconLocation(ZOMBIE_CAR_IMAGE_FILE);
	}
}

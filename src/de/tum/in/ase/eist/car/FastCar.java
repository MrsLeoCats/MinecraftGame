package de.tum.in.ase.eist.car;

import de.tum.in.ase.eist.Dimension2D;


public class FastCar extends Car {

	private static final String ZOMBIE_CAR_IMAGE_FILE = "ZombieCar.jpg";

	private static final double MIN_SPEED_ZOMBIE_CAR = 0.5;
	private static final double MAX_SPEED_ZOMBIE_CAR = 3.5;
	private static final double ZOMBIE_ACCELERATION = 0.01;

	public FastCar(Dimension2D gameBoardSize) {
		super(gameBoardSize);
		setMinSpeed(MIN_SPEED_ZOMBIE_CAR);
		setMaxSpeed(MAX_SPEED_ZOMBIE_CAR);
		setRandomSpeed();
		setIconLocation(ZOMBIE_CAR_IMAGE_FILE);
		setAcceleration(ZOMBIE_ACCELERATION);
	}


}

package de.tum.in.ase.eist.car;

import de.tum.in.ase.eist.Dimension2D;

public class SlowCar extends Car {

	private static final String SKELETON_CAR_IMAGE_FILE = "SkeletonCar.jpg";

	private static final int MIN_SPEED_SLOW_CAR = 200;
	private static final int MAX_SPEED_SLOW_CAR = 500;

	public SlowCar(Dimension2D gameBoardSize) {
		super(gameBoardSize);
		setMinSpeed(MIN_SPEED_SLOW_CAR);
		setMaxSpeed(MAX_SPEED_SLOW_CAR);
		setRandomSpeed();
		setIconLocation(SKELETON_CAR_IMAGE_FILE);
	}
}

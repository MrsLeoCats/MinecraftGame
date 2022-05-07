package de.tum.in.ase.eist.car;

import de.tum.in.ase.eist.Dimension2D;

public class SlowCar extends Car {

	private static final String SKELETON_CAR_IMAGE_FILE = "SkeletonCar.jpg";

	private static final double MIN_SPEED_SLOW_CAR = 2.0;
	private static final double MAX_SPEED_SLOW_CAR = 5.0;

	public SlowCar(Dimension2D gameBoardSize) {
		super(gameBoardSize);
		setMinSpeed(MIN_SPEED_SLOW_CAR);
		setMaxSpeed(MAX_SPEED_SLOW_CAR);
		setRandomSpeed();
		setIconLocation(SKELETON_CAR_IMAGE_FILE);
	}
}

package de.tum.in.ase.eist.car;

import de.tum.in.ase.eist.Dimension2D;
import de.tum.in.ase.eist.Player;

public abstract class CollectableCar extends Car {

    private static final int ONE_I = 1, ZERO_I = 0;
    private static final double ONE = 1.0, ZERO = 0.0;

    protected CollectableCar(Dimension2D gameBoardSize) {
        super(gameBoardSize, ONE_I);
        setMaxSpeed(ONE);
        setSpeed(ZERO);
        setAcceleration(ZERO);
        this.crunchesOnBorder = true;
        this.knockBackApplier = false;
        this.hostile = false;
        this.setAttack(ZERO_I);
    }

    public abstract void onPickup(Player player);

}

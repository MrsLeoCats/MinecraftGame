package de.tum.in.ase.eist.car;

import de.tum.in.ase.eist.Dimension2D;
import de.tum.in.ase.eist.Player;

public abstract class CollectableCar extends Car {

    private static final int ONE_I = 1;
    private static final int ZERO_I = 0;
    private static final double ONE = 1.0;
    private static final double ZERO = 0.0;

    protected CollectableCar(Dimension2D gameBoardSize) {
        super(gameBoardSize, ONE_I);
        setMaxSpeed(ONE);
        setSpeed(ZERO);
        setAcceleration(ZERO);
        setCrunchesOnBorder(true);
        setKnockBackApplier(false);
        setHostile(false);
        this.setAttack(ZERO_I);
    }

    public abstract void onPickup(Player player);

}

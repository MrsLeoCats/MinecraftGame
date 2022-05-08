package de.tum.in.ase.eist.car;

import de.tum.in.ase.eist.Dimension2D;
import de.tum.in.ase.eist.Player;

public abstract class CollectableCar extends Car {

    protected CollectableCar(Dimension2D gameBoardSize) {
        super(gameBoardSize, 1);
        setMaxSpeed(1.0);
        setSpeed(0.0);
        setAcceleration(0.0);
        this.crunchesOnBorder = true;
        this.knockBackApplier = false;
        this.hostile = false;
    }

    public abstract void onPickup(Player player);

}
